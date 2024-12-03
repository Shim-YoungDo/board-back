package com.board.back.controller;

import com.board.back.model.Board;
import com.board.back.model.Result;
import com.board.back.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class BoardController {

    @Autowired
    private BoardService boardService;

    /**
     * 1.1 게시글 목록 조회
     * @return
     */
    @GetMapping("/board")
    public Result getAllBoards(Pageable pageable){
        return boardService.getAllBoard(pageable);
    }

    @PostMapping("/board")
    public Result createBoard(@RequestBody Board board, HttpServletRequest request){
        return boardService.createBoard(board, request);
    }

    @GetMapping("/board/{no}")
    public Result getBoardDetail(@PathVariable Integer no, HttpServletRequest request){
        return boardService.boardDetail(no, request);
    }

    @PutMapping("/board/{no}")
    public Result updateBoardByNo(@PathVariable Integer no, @RequestBody Board board, HttpServletRequest request) {

        return boardService.updateBoard(no, board, request);
    }

    @DeleteMapping("/board/{no}")
    public Result deleteBoard(@PathVariable Integer no){
        return boardService.deleteBoard(no);
    }
}
