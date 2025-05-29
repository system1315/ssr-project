package com.kh.board.service;

import com.kh.board.dto.BoardDTO;
import com.kh.board.dto.PageDTO;
import com.kh.board.dto.SearchDTO;
import com.kh.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;

    @Override
    public PageDTO<BoardDTO> getAllBoards(SearchDTO searchDTO) {
        return boardRepository.findAll(searchDTO);
    }

    @Override
    public BoardDTO getBoard(Long id) {
        return boardRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
    }

    @Override
    @Transactional
    public BoardDTO createBoard(BoardDTO board) {
        board.setCreatedDate(LocalDateTime.now());
        board.setModifiedDate(LocalDateTime.now());
        return boardRepository.save(board);
    }

    @Override
    @Transactional
    public BoardDTO updateBoard(Long id, BoardDTO board) {
        BoardDTO existingBoard = getBoard(id);
        existingBoard.setTitle(board.getTitle());
        existingBoard.setContent(board.getContent());
        existingBoard.setModifiedDate(LocalDateTime.now());
        return boardRepository.save(existingBoard);
    }

    @Override
    @Transactional
    public void deleteBoard(Long id) {
        boardRepository.deleteById(id);
    }
}