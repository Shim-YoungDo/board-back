package com.board.back.controller;

import com.board.back.model.Member;
import com.board.back.model.Result;
import com.board.back.service.BoardService;
import com.board.back.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/customer")
public class MemberController {

    @Autowired
    private MemberService memberService;

    /**
     * 회원가입
     * @return
     */
    @PostMapping("/v1/customers/member")
    public Result insertMember(@RequestBody Member member){
        return memberService.insertMember(member);
    }

    /**
     * 아이디 중복 체크
     * @param member
     * @return
     */
    @PostMapping("/v1/customers/idCheck")
    public Result idDupCheck(@RequestBody Member member){
        return memberService.idDupCheck(member);
    }

    /**
     * 로그인 요청
     * @param _member
     * @param request
     * @return
     */
    @PostMapping("/v1/customers/login")
    public Result loginMember(@RequestBody Member _member, HttpServletRequest request) {
        HttpSession session = request.getSession();
        return memberService.loginMember(_member, session);
    }

    /**
     * 로그아웃
     * @param request
     * @return
     */
    @GetMapping("/v1/customers/logout")
    public Result logoutMember(HttpServletRequest request){
        Result result = new Result();
        HttpSession session = request.getSession();

        if(session != null){
            session.invalidate();
        }

        result.setResultCode(Result.RESULT_CODE.SUCCESS);
        result.setResultMessage("로그아웃이 되었습니다.");
        return result;
    }
}
