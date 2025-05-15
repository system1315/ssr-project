package com.kh.board.repository;

import com.kh.board.dto.BoardDTO;
import com.kh.board.dto.PageDTO;
import com.kh.board.dto.SearchDTO;

import java.util.List;
import java.util.Optional;

public interface BoardRepository {
  PageDTO<BoardDTO> findAll(SearchDTO searchDTO);
  Optional<BoardDTO> findById(Long id);
  BoardDTO save(BoardDTO board);
  void deleteById(Long id);
}