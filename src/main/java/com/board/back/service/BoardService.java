package com.board.back.service;

import com.board.back.model.Board;
import com.board.back.model.Result;
import com.board.back.repository.BoardRepository;
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

    public Result createBoard(Board board){
        Result result = new Result();

        try{
            // TODO validate 추가 필요
            boardRepository.save(board);
            result.setResultCode(Result.RESULT_CODE.SUCCESS);
        } catch(Exception e){
            result.setResultCode(Result.RESULT_CODE.ERROR);
            result.setResultMessage(e.getMessage());
        }

        return result;
    }

    public Result boardDetail(Integer no){
        Result result = new Result();
        try{
            Board board = boardRepository.findById(no)
                    .orElseThrow(() -> new Exception("Not exist Board Data by no : ["+no+"]"));

            board.setMemberId(board.getMemberId().replaceAll(".{2}$", "**"));
            result.setResultCode(Result.RESULT_CODE.SUCCESS);
            JSONObject json = new JSONObject(board);
            json.put("updateAvalYn", "Y");
            System.out.println("json = " + json.toString());
            result.setData(board);
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
