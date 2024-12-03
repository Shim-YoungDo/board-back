package com.board.back.service;

import com.board.back.model.Board;
import com.board.back.model.Result;
import com.board.back.repository.BoardRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    public Result getAllBoard(Pageable pageable){
        Result result = new Result();

        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "createdTime"));

        Page<Board> board = boardRepository.findAll(pageable);
        List<Board> boardList = board.getContent();


        for(Board board2 : boardList){
            board2.setMemberId(board2.getMemberId().replaceAll(".{2}$", "**"));
        }

        JSONObject json = new JSONObject();
        json.put("boardList", boardList);
        json.put("pageNo", board.getNumber());
        json.put("pageSize", board.getSize());

        result.setResultCode(Result.RESULT_CODE.SUCCESS);
        result.setData(json.toString());

        return result;
    }

    public Result createBoard(Board board, HttpServletRequest request){
        Result result = new Result();
        HttpSession session = request.getSession();

        String sId = (String) session.getAttribute("SID");

        try{
            // TODO validate 추가 필요
            if(board.getTitle().length() > 50){
                result.setResultCode(Result.RESULT_CODE.FAIL);
                result.setResultMessage("제목은 최대 50글자까지 가능합니다.");
                return result;
            }

            if(board.getContents().length() > 100){
                result.setResultCode(Result.RESULT_CODE.FAIL);
                result.setResultMessage("제목은 최대 100글자까지 가능합니다.");
                return result;
            }

            board.setMemberId(sId);
            boardRepository.save(board);
            result.setResultCode(Result.RESULT_CODE.SUCCESS);
        } catch(Exception e){
            result.setResultCode(Result.RESULT_CODE.ERROR);
            result.setResultMessage(e.getMessage());
        }

        return result;
    }

    public Result boardDetail(Integer no, HttpServletRequest request){
        Result result = new Result();
        HttpSession session = request.getSession(false);
        String sId = "";

        try{
            Board board = boardRepository.findById(no)
                    .orElseThrow(() -> new Exception("Not exist Board Data by no : ["+no+"]"));

            if(session != null && session.getAttribute("SID") != null){
                sId = (String) session.getAttribute("SID");
            }
            String updateAvalYn =sId.equals(board.getMemberId()) ? "Y" : "N"; // 수정 가능 여부

            board.setMemberId(board.getMemberId().replaceAll(".{2}$", "**")); // 화면에 노출될 ID 마스킹 처리(뒤 2자리)
            result.setResultCode(Result.RESULT_CODE.SUCCESS);
            JSONObject json = new JSONObject(board);

            json.put("updateAvalYn", updateAvalYn);
            result.setData(json.toString());
        }catch (Exception e){
            result.setResultCode(Result.RESULT_CODE.ERROR);
            result.setResultMessage(e.getMessage());
        }

        return result;
    }

    public Result deleteBoard(Integer no){
        Result result = new Result();
        try{
            Board board = boardRepository.findById(no)
                    .orElseThrow(() -> new Exception("Not exist Board Data by no : ["+no+"]"));
            boardRepository.delete(board);
            result.setResultCode(Result.RESULT_CODE.SUCCESS);
            result.setData(board);
        }catch (Exception e){
            result.setResultCode(Result.RESULT_CODE.ERROR);
            result.setResultMessage(e.getMessage());
        }

        return result;
    }
}
