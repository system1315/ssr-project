package com.kh.board.controller;

import com.kh.board.dto.BoardDTO;
import com.kh.board.dto.SearchDTO;
import com.kh.board.dto.MemberDTO;
import com.kh.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;

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
  public String writeForm(Model model, HttpSession session) {
    MemberDTO member = (MemberDTO) session.getAttribute("LOGIN_MEMBER");
    if (member == null) {
      return "redirect:/auth/login";
    }
    model.addAttribute("board", new BoardDTO());
    return "board/write";
  }

  @PostMapping("/write")
  public String write(@ModelAttribute BoardDTO board,
                      RedirectAttributes redirectAttributes,
                      HttpSession session) {
    MemberDTO member = (MemberDTO) session.getAttribute("LOGIN_MEMBER");
    if (member == null) {
      return "redirect:/auth/login";
    }
    try {
      if (isValid(board)) {
        board.setAuthor(member.getNickname());
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

  @PostMapping("/api/write")
  public ResponseEntity<?> apiWrite(@RequestBody BoardDTO board, HttpSession session) {
    MemberDTO member = (MemberDTO) session.getAttribute("LOGIN_MEMBER");
    if (member == null) {
      return ResponseEntity.status(401).body("로그인 필요");
    }
    board.setAuthor(member.getNickname());
    boardService.createBoard(board);
    return ResponseEntity.ok(board);
  }

  @PutMapping("/api/edit/{id}")
  public ResponseEntity<?> apiEdit(@PathVariable("id") Long id, @RequestBody BoardDTO board, HttpSession session) {
    MemberDTO member = (MemberDTO) session.getAttribute("LOGIN_MEMBER");
    if (member == null) {
      return ResponseEntity.status(401).body("로그인 필요");
    }
    board.setAuthor(member.getNickname());
    boardService.updateBoard(id, board);
    return ResponseEntity.ok(board);
  }

  @DeleteMapping("/api/delete/{id}")
  public ResponseEntity<?> apiDelete(@PathVariable("id") Long id, HttpSession session) {
    MemberDTO member = (MemberDTO) session.getAttribute("LOGIN_MEMBER");
    if (member == null) {
      return ResponseEntity.status(401).body("로그인 필요");
    }
    boardService.deleteBoard(id);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/api/boards")
  public ResponseEntity<?> getBoards(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(required = false) String searchType,
      @RequestParam(required = false) String keyword
  ) {
      SearchDTO searchDTO = new SearchDTO();
      searchDTO.setPage(page);
      searchDTO.setSize(size);
      searchDTO.setSearchType(searchType);
      searchDTO.setKeyword(keyword);
      return ResponseEntity.ok(boardService.getAllBoards(searchDTO));
  }

  private boolean isValid(BoardDTO board) {
    return board.getTitle() != null && !board.getTitle().trim().isEmpty() &&
        board.getContent() != null && !board.getContent().trim().isEmpty();
  }
}