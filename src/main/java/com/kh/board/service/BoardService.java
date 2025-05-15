package com.kh.board.service;

import com.kh.board.dto.BoardDTO;
import com.kh.board.dto.PageDTO;
import com.kh.board.dto.SearchDTO;

public interface BoardService {
  PageDTO<BoardDTO> getAllBoards(SearchDTO searchDTO);
  BoardDTO getBoard(Long id);
  BoardDTO createBoard(BoardDTO board);
  BoardDTO updateBoard(Long id, BoardDTO board);
  void deleteBoard(Long id);
}