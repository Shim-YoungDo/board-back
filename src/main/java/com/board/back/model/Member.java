package com.board.back.model;

import jakarta.persistence.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "member")
@DynamicInsert
@DynamicUpdate
public class Member {
    @Id
    @Column(name="MEMBER_ID")
    private String memberId;

    @Column(name="MEMBER_PW")
    private String memberPw;

    @Column(name="MEMBER_NM")
    private String memberName;

    @Column(name="EMAIL")
    private String email;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberPw() {
        return memberPw;
    }

    public void setMemberPw(String memberPw) {
        this.memberPw = memberPw;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
