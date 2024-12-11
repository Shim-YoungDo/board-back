package com.board.back.service;

import com.board.back.model.Member;
import com.board.back.model.Result;
import com.board.back.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    /**
     * 회원가입 처리
     * @param member
     * @return
     */
    public Result insertMember(Member member){
        Result result = new Result();

        try{
            memberRepository.save(member);
            result.setResultCode(Result.RESULT_CODE.SUCCESS);
        }catch (Exception e){
            result.setResultCode(Result.RESULT_CODE.ERROR);
            result.setResultMessage(e.getMessage());
        }

        return result;
    }

    /**
     * 아이디 중복 체크
     * @param _member
     * @return
     */
    public Result idDupCheck(Member _member){
        Result result = new Result();

        try{
            Member member = memberRepository.findByMemberId(_member.getMemberId());
            if (member != null) {
                result.setResultCode(Result.RESULT_CODE.FAIL);
                result.setResultMessage("중복된 아이디입니다. 다시 입력해주세요.");
                return  result;
            }
            result.setResultCode(Result.RESULT_CODE.SUCCESS);
            result.setResultMessage("사용 가능한 아이디입니다.");
        }catch (Exception e){
            result.setResultCode(Result.RESULT_CODE.ERROR);
            result.setResultMessage(e.getMessage());
        }
        return result;
    }

    /**
     * 로그인 처리
     * @param _member
     * @param session
     * @return
     */
    public Result loginMember(Member _member, HttpSession session){
        Result result = new Result();
        try{
            // 회원정보 조회
            Member member = memberRepository.findByMemberIdAndMemberPw(_member.getMemberId(), _member.getMemberPw());
            if (member == null) {
                result.setResultCode(Result.RESULT_CODE.FAIL);
                result.setResultMessage("아이디 또는 비밀번호가 일치하지 않습니다.");
                return  result;
            }
            session.setAttribute("SID", member.getMemberId());
            session.setAttribute("SNAME", member.getMemberName());

            JSONObject json = new JSONObject();
            json.put("name", member.getMemberName()); // 프론트 표시용

            result.setResultCode(Result.RESULT_CODE.SUCCESS);
            result.setData(json.toString());
        } catch (Exception e){
            result.setResultCode(Result.RESULT_CODE.ERROR);
            result.setResultMessage(e.getMessage());
        }
        return result;
    }
}
