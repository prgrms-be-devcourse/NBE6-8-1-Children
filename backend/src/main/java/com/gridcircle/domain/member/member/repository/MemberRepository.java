package com.gridcircle.domain.member.member.repository;

import com.gridcircle.domain.member.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {

    Optional<Member> findByEmail(String email);
    Optional<Member> findByApiKey(String apiKey);
//    Optional<Object> findByEmail(String email);
}
