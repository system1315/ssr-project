package com.kh.board.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberDTO {
    private Long id;
    private String email;
    private String password;
    private String nickname;
    private LocalDateTime createdDate;

    public MemberDTO() {}

    public MemberDTO(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.createdDate = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) {
        // null로 들어오면 현재 시간으로 세팅
        if (createdDate == null) {
            this.createdDate = LocalDateTime.now();
        } else {
            this.createdDate = createdDate;
        }
    }
}