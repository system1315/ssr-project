package com.kh.board.repository;

import com.kh.board.dto.MemberDTO;
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
public class JdbcMemberRepository implements MemberRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<MemberDTO> memberRowMapper = (rs, rowNum) -> {
        MemberDTO member = new MemberDTO();
        member.setId(rs.getLong("ID"));
        member.setEmail(rs.getString("EMAIL"));
        member.setPassword(rs.getString("PASSWORD"));
        member.setNickname(rs.getString("NICKNAME"));
        member.setCreatedDate(rs.getTimestamp("CREATED_DATE").toLocalDateTime());
        return member;
    };

    public JdbcMemberRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<MemberDTO> findByEmail(String email) {
        String sql = "SELECT * FROM MEMBER WHERE EMAIL = ?";
        List<MemberDTO> results = jdbcTemplate.query(sql, memberRowMapper, email);
        return results.stream().findFirst();
    }

    @Override
    public Optional<MemberDTO> findByNickname(String nickname) {
        String sql = "SELECT * FROM MEMBER WHERE NICKNAME = ?";
        List<MemberDTO> results = jdbcTemplate.query(sql, memberRowMapper, nickname);
        return results.stream().findFirst();
    }

    @Override
    public Optional<MemberDTO> findByPhone(String phone) {
        String sql = "SELECT * FROM MEMBER WHERE PHONE = ?";
        List<MemberDTO> results = jdbcTemplate.query(sql, memberRowMapper, phone);
        return results.stream().findFirst();
    }

    @Override
    public Optional<MemberDTO> findById(Long id) {
        String sql = "SELECT * FROM MEMBER WHERE ID = ?";
        List<MemberDTO> results = jdbcTemplate.query(sql, memberRowMapper, id);
        return results.stream().findFirst();
    }

    @Override
    public MemberDTO save(MemberDTO member) {
        if (member.getId() == null) {
            return insert(member);
        } else {
            return update(member);
        }
    }

    private MemberDTO insert(MemberDTO member) {
        String sql = "INSERT INTO MEMBER (EMAIL, PASSWORD, NICKNAME, PHONE, CREATED_DATE) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"ID"});
            ps.setString(1, member.getEmail());
            ps.setString(2, member.getPassword());
            ps.setString(3, member.getNickname());
            ps.setString(4, member.getPhone());
            ps.setTimestamp(5, Timestamp.valueOf(member.getCreatedDate()));
            return ps;
        }, keyHolder);
        member.setId(keyHolder.getKey().longValue());
        return member;
    }

    private MemberDTO update(MemberDTO member) {
        String sql = "UPDATE MEMBER SET PASSWORD = ?, NICKNAME = ?, PHONE = ? WHERE ID = ?";
        jdbcTemplate.update(sql, member.getPassword(), member.getNickname(), member.getPhone(), member.getId());
        return member;
    }
} 