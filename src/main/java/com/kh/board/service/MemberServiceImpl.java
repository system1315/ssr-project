package com.kh.board.service;

import com.kh.board.dto.MemberDTO;
import com.kh.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    @Override
    public MemberDTO register(MemberDTO member) {
        member.setCreatedDate(java.time.LocalDateTime.now());
        return memberRepository.save(member);
    }

    @Override
    public Optional<MemberDTO> login(String email, String password) {
        return memberRepository.findByEmail(email)
                .filter(m -> m.getPassword().equals(password));
    }

    @Override
    public Optional<MemberDTO> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    @Override
    public Optional<MemberDTO> findById(Long id) {
        return memberRepository.findById(id);
    }
} 