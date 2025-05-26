package com.kh.board.service;

import com.kh.board.dto.CommentDTO;
import com.kh.board.dto.PageDTO;
import java.util.List;

public interface CommentService {
    List<CommentDTO> getCommentsByBoardId(Long boardId);
    CommentDTO getComment(Long id);
    CommentDTO createComment(CommentDTO comment);
    CommentDTO updateComment(Long id, CommentDTO comment);
    void deleteComment(Long id);
    PageDTO<CommentDTO> getCommentsByBoardIdPaged(Long boardId, int page, int size);
} 