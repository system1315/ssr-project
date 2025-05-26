package com.kh.board.repository;

import com.kh.board.dto.CommentDTO;
import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    List<CommentDTO> findByBoardId(Long boardId);
    Optional<CommentDTO> findById(Long id);
    CommentDTO save(CommentDTO comment);
    void deleteById(Long id);
} 