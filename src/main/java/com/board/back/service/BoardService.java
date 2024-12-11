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

import java.util.Date;
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

        if(session == null && session.getAttribute("SID") == null ){
            result.setResultCode(Result.RESULT_CODE.FAIL);
            result.setResultMessage("장시간 입력이 없어 정보를 가져올 수 없습니다. 다시 진행해주세요.");
            result.setApiResultCode("0099");
            return result;
        }
        String sId = (String) session.getAttribute("SID");

        try{
            Result validResult = this.validationCheck(board);
            if(!validResult.getResultCode().equals(Result.RESULT_CODE.SUCCESS)){
                return validResult;
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

    public Result updateBoard(Integer no, Board updatedBoard, HttpServletRequest request) {
        Result result = new Result();
        HttpSession session = request.getSession();
        if(session == null && session.getAttribute("SID") == null ){
            result.setResultCode(Result.RESULT_CODE.FAIL);
            result.setResultMessage("장시간 입력이 없어 정보를 가져올 수 없습니다. 다시 진행해주세요.");
            result.setApiResultCode("0099");
            return result;
        }

        try{
            Result validResult = this.validationCheck(updatedBoard);
            if(!validResult.getResultCode().equals(Result.RESULT_CODE.SUCCESS)){
                return validResult;
            }

            Board board = boardRepository.findById(no)
                    .orElseThrow(() -> new Exception("Not exist Board Data by no : ["+no+"]"));

            if(!session.getAttribute("SID").equals(board.getMemberId())){
                // 작성자와 로그인한 사용자 체크
                result.setResultCode(Result.RESULT_CODE.FAIL);
                result.setResultMessage("수정 권한이 없습니다.");
                return  result;
            }

            board.setType(updatedBoard.getType());
            board.setTitle(updatedBoard.getTitle());
            board.setContents(updatedBoard.getContents());
            board.setUpdatedTime(new Date());

            boardRepository.save(board);
            result.setResultCode(Result.RESULT_CODE.SUCCESS);
        }catch(Exception e){
            result.setResultCode(Result.RESULT_CODE.ERROR);
            result.setResultMessage(e.getMessage());
        }
        return result;
    }

    /**
     * 입력값 유효성 검증
     * @param board
     * @return
     */
    public Result validationCheck(Board board){
        Result result = new Result();
        if(board.getTitle().length() > 50){
            result.setResultCode(Result.RESULT_CODE.FAIL);
            result.setResultMessage("제목은 최대 50글자까지 가능합니다.");
            return result;
        }

        if(board.getContents().length() > 100){
            result.setResultCode(Result.RESULT_CODE.FAIL);
            result.setResultMessage("내용은 최대 100글자까지 가능합니다.");
            return result;
        }

        result.setResultCode(Result.RESULT_CODE.SUCCESS);
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

    public Result deleteBoard(Integer no, HttpServletRequest request){
        Result result = new Result();
        HttpSession session = request.getSession();

        if(session == null && session.getAttribute("SID") == null ){
            result.setResultCode(Result.RESULT_CODE.FAIL);
            result.setResultMessage("장시간 입력이 없어 정보를 가져올 수 없습니다. 다시 진행해주세요.");
            result.setApiResultCode("0099");
            return result;
        }
        
        try{
            Board board = boardRepository.findById(no)
                    .orElseThrow(() -> new Exception("Not exist Board Data by no : ["+no+"]"));
           
            if(!session.getAttribute("SID").equals(board.getMemberId())){
                // 작성자와 로그인한 사용자 체크
                result.setResultCode(Result.RESULT_CODE.FAIL);
                result.setResultMessage("삭제 권한이 없습니다.");
                return  result;
            }
            
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
