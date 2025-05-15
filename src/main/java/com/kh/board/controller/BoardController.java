package com.kh.board.controller;

import com.kh.board.dto.BoardDTO;
import com.kh.board.dto.SearchDTO;
import com.kh.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
  private final BoardService boardService;

  @GetMapping({"", "/"})
  public String index() {
    return "redirect:/board/list";
  }

  @GetMapping("/list")
  public String list(@ModelAttribute SearchDTO searchDTO, Model model) {
    model.addAttribute("result", boardService.getAllBoards(searchDTO));
    model.addAttribute("search", searchDTO);
    return "board/list";
  }

  @GetMapping("/write")
  public String writeForm(Model model) {
    model.addAttribute("board", new BoardDTO());
    return "board/write";
  }

  @PostMapping("/write")
  public String write(@ModelAttribute BoardDTO board,
                      RedirectAttributes redirectAttributes) {
    try {
      if (isValid(board)) {
        boardService.createBoard(board);
        redirectAttributes.addFlashAttribute("message", "게시글이 성공적으로 작성되었습니다.");
        return "redirect:/board/list";
      } else {
        redirectAttributes.addFlashAttribute("error", "필수 항목을 모두 입력해주세요.");
        return "redirect:/board/write";
      }
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "게시글 작성 중 오류가 발생했습니다.");
      return "redirect:/board/write";
    }
  }

  @GetMapping("/view/{id}")
  public String view(@PathVariable("id") Long id, Model model) {
    try {
      model.addAttribute("board", boardService.getBoard(id));
      return "board/view";
    } catch (Exception e) {
      return "redirect:/board/list";
    }
  }

  @GetMapping("/edit/{id}")
  public String editForm(@PathVariable("id") Long id, Model model) {
    try {
      model.addAttribute("board", boardService.getBoard(id));
      return "board/edit";
    } catch (Exception e) {
      return "redirect:/board/list";
    }
  }

  @PostMapping("/edit/{id}")
  public String edit(@PathVariable("id") Long id,
                     @ModelAttribute BoardDTO board,
                     RedirectAttributes redirectAttributes) {
    try {
      if (isValid(board)) {
        boardService.updateBoard(id, board);
        redirectAttributes.addFlashAttribute("message", "게시글이 성공적으로 수정되었습니다.");
        return "redirect:/board/view/" + id;
      } else {
        redirectAttributes.addFlashAttribute("error", "필수 항목을 모두 입력해주세요.");
        return "redirect:/board/edit/" + id;
      }
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "게시글 수정 중 오류가 발생했습니다.");
      return "redirect:/board/edit/" + id;
    }
  }

  @PostMapping("/delete/{id}")
  public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
    try {
      boardService.deleteBoard(id);
      redirectAttributes.addFlashAttribute("message", "게시글이 성공적으로 삭제되었습니다.");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "게시글 삭제 중 오류가 발생했습니다.");
    }
    return "redirect:/board/list";
  }

  private boolean isValid(BoardDTO board) {
    return board.getTitle() != null && !board.getTitle().trim().isEmpty() &&
        board.getContent() != null && !board.getContent().trim().isEmpty() &&
        board.getAuthor() != null && !board.getAuthor().trim().isEmpty();
  }
}