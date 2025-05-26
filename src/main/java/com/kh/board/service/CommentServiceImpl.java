package com.kh.board.service;

import com.kh.board.dto.CommentDTO;
import com.kh.board.dto.PageDTO;
import com.kh.board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    @Override
    public List<CommentDTO> getCommentsByBoardId(Long boardId) {
        return commentRepository.findByBoardId(boardId);
    }

    @Override
    public CommentDTO getComment(Long id) {
        return commentRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
    }

    @Override
    @Transactional
    public CommentDTO createComment(CommentDTO comment) {
        if (comment.getBoardId() == null || comment.getContent() == null || comment.getAuthor() == null
            || comment.getContent().trim().isEmpty() || comment.getAuthor().trim().isEmpty()) {
            throw new IllegalArgumentException("boardId, content, author는 필수입니다.");
        }
        System.out.println("[DEBUG] 댓글 저장: boardId=" + comment.getBoardId() + ", content=" + comment.getContent() + ", author=" + comment.getAuthor());
        comment.setCreatedDate(LocalDateTime.now());
        comment.setModifiedDate(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public CommentDTO updateComment(Long id, CommentDTO comment) {
        CommentDTO existingComment = getComment(id);
        existingComment.setContent(comment.getContent());
        existingComment.setModifiedDate(LocalDateTime.now());
        return commentRepository.save(existingComment);
    }

    @Override
    @Transactional
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public PageDTO<CommentDTO> getCommentsByBoardIdPaged(Long boardId, int page, int size) {
        List<CommentDTO> all = commentRepository.findByBoardId(boardId);
        int totalElements = all.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        int fromIndex = Math.max(0, (page - 1) * size);
        int toIndex = Math.min(fromIndex + size, totalElements);
        List<CommentDTO> content = all.subList(fromIndex, toIndex);
        return new PageDTO<>(content, page, totalPages, totalElements, size);
    }
} 