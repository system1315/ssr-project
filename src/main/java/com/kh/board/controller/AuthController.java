package com.kh.board.controller;

import com.kh.board.dto.MemberDTO;
import com.kh.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final MemberService memberService;
    private static final String LOGIN_MEMBER = "LOGIN_MEMBER";

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody MemberDTO member) {
        if (memberService.findByEmail(member.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("이미 존재하는 이메일입니다.");
        }
        MemberDTO saved = memberService.register(member);
        return ResponseEntity.ok(saved);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Map<String, String> req, HttpSession session) {
        String email = req.get("email");
        String password = req.get("password");
        return memberService.login(email, password)
            .map(member -> {
                session.setAttribute(LOGIN_MEMBER, member);
                return (Object) member;
            })
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.status(401).body("이메일 또는 비밀번호가 올바르지 않습니다."));
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().build();
    }

    // 로그인 상태 확인
    @GetMapping("/me")
    public ResponseEntity<?> me(HttpSession session) {
        MemberDTO member = (MemberDTO) session.getAttribute(LOGIN_MEMBER);
        if (member == null) {
            return ResponseEntity.status(401).body("로그인 필요");
        }
        Map<String, Object> result = new HashMap<>();
        result.put("id", member.getId());
        result.put("email", member.getEmail());
        result.put("nickname", member.getNickname());
        return ResponseEntity.ok(result);
    }
} 