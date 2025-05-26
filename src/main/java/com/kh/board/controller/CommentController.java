package com.kh.board.controller;

import com.kh.board.dto.CommentDTO;
import com.kh.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.kh.board.dto.MemberDTO;
import jakarta.servlet.http.HttpSession;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/board/{boardId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByBoardId(@PathVariable Long boardId) {
        return ResponseEntity.ok(commentService.getCommentsByBoardId(boardId));
    }

    @GetMapping("/board/{boardId}/paged")
    public ResponseEntity<?> getCommentsByBoardIdPaged(
        @PathVariable Long boardId,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(commentService.getCommentsByBoardIdPaged(boardId, page, size));
    }

    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody CommentDTO comment, HttpSession session) {
        MemberDTO member = (MemberDTO) session.getAttribute("LOGIN_MEMBER");
        if (member == null) {
            return ResponseEntity.status(401).body("로그인 필요");
        }
        comment.setAuthor(member.getNickname());
        return ResponseEntity.ok(commentService.createComment(comment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateComment(@PathVariable Long id, @RequestBody CommentDTO comment, HttpSession session) {
        MemberDTO member = (MemberDTO) session.getAttribute("LOGIN_MEMBER");
        if (member == null) {
            return ResponseEntity.status(401).body("로그인 필요");
        }
        comment.setAuthor(member.getNickname());
        return ResponseEntity.ok(commentService.updateComment(id, comment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id, HttpSession session) {
        MemberDTO member = (MemberDTO) session.getAttribute("LOGIN_MEMBER");
        if (member == null) {
            return ResponseEntity.status(401).body("로그인 필요");
        }
        commentService.deleteComment(id);
        return ResponseEntity.ok().build();
    }
} 