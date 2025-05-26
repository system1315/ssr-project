package com.kh.board.repository;

import com.kh.board.dto.CommentDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcCommentRepository implements CommentRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<CommentDTO> commentRowMapper = (rs, rowNum) -> {
        CommentDTO comment = new CommentDTO();
        comment.setId(rs.getLong("ID"));
        comment.setBoardId(rs.getLong("BOARD_ID"));
        comment.setContent(rs.getString("CONTENT"));
        comment.setAuthor(rs.getString("AUTHOR"));
        comment.setCreatedDate(rs.getTimestamp("CREATED_DATE").toLocalDateTime());
        comment.setModifiedDate(rs.getTimestamp("MODIFIED_DATE").toLocalDateTime());
        return comment;
    };

    public JdbcCommentRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<CommentDTO> findByBoardId(Long boardId) {
        String sql = "SELECT * FROM BOARD_COMMENT WHERE BOARD_ID = ? ORDER BY CREATED_DATE DESC";
        return jdbcTemplate.query(sql, commentRowMapper, boardId);
    }

    @Override
    public Optional<CommentDTO> findById(Long id) {
        String sql = "SELECT * FROM BOARD_COMMENT WHERE ID = ?";
        List<CommentDTO> results = jdbcTemplate.query(sql, commentRowMapper, id);
        return results.stream().findFirst();
    }

    @Override
    public CommentDTO save(CommentDTO comment) {
        return comment.getId() == null ? insert(comment) : update(comment);
    }

    private CommentDTO insert(CommentDTO comment) {
        String sql = "INSERT INTO BOARD_COMMENT (BOARD_ID, CONTENT, AUTHOR, CREATED_DATE, MODIFIED_DATE) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"ID"});
            ps.setLong(1, comment.getBoardId());
            ps.setString(2, comment.getContent());
            ps.setString(3, comment.getAuthor());
            ps.setTimestamp(4, Timestamp.valueOf(comment.getCreatedDate()));
            ps.setTimestamp(5, Timestamp.valueOf(comment.getModifiedDate()));
            return ps;
        }, keyHolder);

        comment.setId(keyHolder.getKey().longValue());
        return comment;
    }

    private CommentDTO update(CommentDTO comment) {
        String sql = "UPDATE BOARD_COMMENT SET CONTENT = ?, MODIFIED_DATE = ? WHERE ID = ?";
        int updated = jdbcTemplate.update(sql,
            comment.getContent(),
            Timestamp.valueOf(comment.getModifiedDate()),
            comment.getId()
        );

        if (updated == 0) {
            throw new RuntimeException("댓글 수정 실패: ID " + comment.getId());
        }
        return comment;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM BOARD_COMMENT WHERE ID = ?";
        int deleted = jdbcTemplate.update(sql, id);
        if (deleted == 0) {
            throw new RuntimeException("댓글 삭제 실패: ID " + id);
        }
    }
} 