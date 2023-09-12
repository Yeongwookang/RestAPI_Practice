package com.culflab.jwtauthsb.member.repository;

import com.culflab.jwtauthsb.member.service.handler.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByAccount(String account);

}
