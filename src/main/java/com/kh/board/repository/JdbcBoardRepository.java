package com.kh.board.repository;

import com.kh.board.dto.BoardDTO;
import com.kh.board.dto.PageDTO;
import com.kh.board.dto.SearchDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcBoardRepository implements BoardRepository {
  private final JdbcTemplate jdbcTemplate;

  private final RowMapper<BoardDTO> boardRowMapper = (rs, rowNum) -> {
    BoardDTO board = new BoardDTO();
    board.setId(rs.getLong("ID"));
    board.setTitle(rs.getString("TITLE"));
    board.setContent(rs.getString("CONTENT"));
    board.setAuthor(rs.getString("AUTHOR"));
    board.setCreatedDate(rs.getTimestamp("CREATED_DATE").toLocalDateTime());
    board.setModifiedDate(rs.getTimestamp("MODIFIED_DATE").toLocalDateTime());
    return board;
  };

  public JdbcBoardRepository(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  @Override
  public PageDTO<BoardDTO> findAll(SearchDTO searchDTO) {
    // 전체 게시글 수 조회를 위한 별도의 파라미터 리스트
    List<Object> countParams = new ArrayList<>();
    String countSql = buildCountQuery(searchDTO, countParams);

    Long totalElements = jdbcTemplate.queryForObject(
        countSql,
        countParams.toArray(),
        Long.class
    );

    // 게시글 목록 조회를 위한 별도의 파라미터 리스트
    List<Object> listParams = new ArrayList<>();
    String listSql = buildSearchQuery(searchDTO, listParams);

    // 페이징 처리를 위한 파라미터 추가
    listParams.add(searchDTO.getOffset());
    listParams.add(searchDTO.getSize());

    List<BoardDTO> content = jdbcTemplate.query(
        listSql + " ORDER BY ID DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY",
        boardRowMapper,
        listParams.toArray()
    );

    int totalPages = (int) Math.ceil((double) totalElements / searchDTO.getSize());
    return new PageDTO<>(content, searchDTO.getPage(), totalPages, totalElements, searchDTO.getSize());
  }

  @Override
  public Optional<BoardDTO> findById(Long id) {
    String sql = "SELECT * FROM BOARD WHERE ID = ?";
    List<BoardDTO> results = jdbcTemplate.query(sql, boardRowMapper, id);
    return results.stream().findFirst();
  }

  @Override
  public BoardDTO save(BoardDTO board) {
    return board.getId() == null ? insert(board) : update(board);
  }

  private BoardDTO insert(BoardDTO board) {
    String sql = "INSERT INTO BOARD (TITLE, CONTENT, AUTHOR, CREATED_DATE, MODIFIED_DATE) VALUES (?, ?, ?, ?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(sql, new String[]{"ID"});
      ps.setString(1, board.getTitle());
      ps.setString(2, board.getContent());
      ps.setString(3, board.getAuthor());
      ps.setTimestamp(4, Timestamp.valueOf(board.getCreatedDate()));
      ps.setTimestamp(5, Timestamp.valueOf(board.getModifiedDate()));
      return ps;
    }, keyHolder);

    board.setId(keyHolder.getKey().longValue());
    return board;
  }

  private BoardDTO update(BoardDTO board) {
    String sql = "UPDATE BOARD SET TITLE = ?, CONTENT = ?, MODIFIED_DATE = ? WHERE ID = ?";
    int updated = jdbcTemplate.update(sql,
        board.getTitle(),
        board.getContent(),
        Timestamp.valueOf(board.getModifiedDate()),
        board.getId()
    );

    if (updated == 0) {
      throw new RuntimeException("게시글 수정 실패: ID " + board.getId());
    }
    return board;
  }

  @Override
  public void deleteById(Long id) {
    String sql = "DELETE FROM BOARD WHERE ID = ?";
    int deleted = jdbcTemplate.update(sql, id);
    if (deleted == 0) {
      throw new RuntimeException("게시글 삭제 실패: ID " + id);
    }
  }

  private String buildSearchQuery(SearchDTO searchDTO, List<Object> params) {
    StringBuilder sql = new StringBuilder("SELECT * FROM BOARD WHERE 1=1");
    appendSearchCondition(sql, searchDTO, params);
    return sql.toString();
  }

  private String buildCountQuery(SearchDTO searchDTO, List<Object> params) {
    StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM BOARD WHERE 1=1");
    appendSearchCondition(sql, searchDTO, params);
    return sql.toString();
  }

  private void appendSearchCondition(StringBuilder sql, SearchDTO searchDTO, List<Object> params) {
    if (searchDTO.getKeyword() != null && !searchDTO.getKeyword().trim().isEmpty()) {
      String keyword = "%" + searchDTO.getKeyword() + "%";

      switch (searchDTO.getSearchType()) {
        case "title":
          sql.append(" AND TITLE LIKE ?");
          params.add(keyword);
          break;
        case "content":
          sql.append(" AND CONTENT LIKE ?");
          params.add(keyword);
          break;
        case "author":
          sql.append(" AND AUTHOR LIKE ?");
          params.add(keyword);
          break;
        case "titleContent":
          sql.append(" AND (TITLE LIKE ? OR CONTENT LIKE ?)");
          params.add(keyword);
          params.add(keyword);
          break;
        case "all":
        default:
          break;
      }
    }
  }
}