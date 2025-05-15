package com.kh.board.jdbc;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class JdbcTemplate {
  private final DataSource dataSource;

  public JdbcTemplate(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public <T> Optional<T> queryForObject(String sql, RowMapper<T> rowMapper, Object... params) {
    return executeQuery(sql, rs -> {
      if (rs.next()) {
        return Optional.ofNullable(rowMapper.mapRow(rs));
      }
      return Optional.empty();
    }, params);
  }

  public <T> List<T> queryForList(String sql, RowMapper<T> rowMapper, Object... params) {
    return executeQuery(sql, rs -> {
      List<T> results = new ArrayList<>();
      while (rs.next()) {
        results.add(rowMapper.mapRow(rs));
      }
      return results;
    }, params);
  }

  public int update(String sql, Object... params) {
    return executeInConnection(conn -> {
      try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        setParameters(pstmt, params);
        return pstmt.executeUpdate();
      }
    });
  }

  public <T> T insert(String sql, KeyHolder keyHolder, Object... params) {
    return executeInConnection(conn -> {
      try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        setParameters(pstmt, params);
        pstmt.executeUpdate();

        try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
          if (generatedKeys.next()) {
            keyHolder.setKey(generatedKeys.getLong(1));
            return (T) keyHolder.getKey();
          }
          throw new SQLException("키 생성 실패");
        }
      }
    });
  }

  private <T> T executeQuery(String sql, ResultSetExtractor<T> extractor, Object... params) {
    return executeInConnection(conn -> {
      try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        setParameters(pstmt, params);
        try (ResultSet rs = pstmt.executeQuery()) {
          return extractor.extractData(rs);
        }
      }
    });
  }

  private <T> T executeInConnection(ConnectionCallback<T> action) {
    try (Connection conn = dataSource.getConnection()) {
      return action.doInConnection(conn);
    } catch (SQLException e) {
      throw new DataAccessException("데이터베이스 작업 중 오류 발생", e);
    }
  }

  private void executeInTransaction(Consumer<Connection> action) {
    try (Connection conn = dataSource.getConnection()) {
      try {
        conn.setAutoCommit(false);
        action.accept(conn);
        conn.commit();
      } catch (SQLException e) {
        conn.rollback();
        throw new DataAccessException("트랜잭션 실행 중 오류 발생", e);
      }
    } catch (SQLException e) {
      throw new DataAccessException("데이터베이스 연결 중 오류 발생", e);
    }
  }

  private void setParameters(PreparedStatement pstmt, Object... params) throws SQLException {
    for (int i = 0; i < params.length; i++) {
      pstmt.setObject(i + 1, params[i]);
    }
  }

  @FunctionalInterface
  public interface RowMapper<T> {
    T mapRow(ResultSet rs) throws SQLException;
  }

  @FunctionalInterface
  public interface ResultSetExtractor<T> {
    T extractData(ResultSet rs) throws SQLException;
  }

  @FunctionalInterface
  private interface ConnectionCallback<T> {
    T doInConnection(Connection connection) throws SQLException;
  }

  public static class KeyHolder {
    private Long key;

    public Long getKey() {
      return key;
    }

    void setKey(Long key) {
      this.key = key;
    }
  }

  public static class DataAccessException extends RuntimeException {
    public DataAccessException(String message, Throwable cause) {
      super(message, cause);
    }
  }
}