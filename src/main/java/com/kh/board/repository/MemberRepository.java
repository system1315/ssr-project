package com.kh.board.repository;

import com.kh.board.dto.MemberDTO;
import java.util.Optional;

public interface MemberRepository {
    Optional<MemberDTO> findByEmail(String email);
    Optional<MemberDTO> findById(Long id);
    MemberDTO save(MemberDTO member);
} 