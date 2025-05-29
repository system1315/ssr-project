package com.kh.board.service;

import com.kh.board.dto.MemberDTO;
import java.util.Optional;

public interface MemberService {
    MemberDTO register(MemberDTO member);
    Optional<MemberDTO> login(String email, String password);
    Optional<MemberDTO> findByEmail(String email);
    Optional<MemberDTO> findById(Long id);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    boolean existsByPhone(String phone);
} 