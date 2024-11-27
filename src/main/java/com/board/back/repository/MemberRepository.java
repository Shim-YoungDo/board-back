package com.board.back.repository;

import com.board.back.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {
    Member findByMemberId(String memberId);
}
