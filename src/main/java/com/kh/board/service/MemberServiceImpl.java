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

    @Override
    public boolean existsByEmail(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return memberRepository.findByNickname(nickname).isPresent();
    }

    @Override
    public boolean existsByPhone(String phone) {
        return memberRepository.findByPhone(phone).isPresent();
    }
} 