package org.tycoon.model.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tycoon.model.member.entity.Member;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("SELECT m FROM Member m WHERE m.userId = :userId AND m.password = :password")
    Optional<Member> findMemberById(@Param("userId") String userId, @Param("password") String password);

    @Query("SELECT m FROM Member m WHERE m.nick = :nick AND m.email = :email")
    Optional<Member> findByNickAndEmail(@Param("nick") String nick, @Param("email") String email);

    @Query("SELECT m FROM Member m WHERE m.nick = :nick AND m.email = :email AND m.userId = :userId")
    Optional<Member> findByNickEmailAndUserId(@Param("nick") String nick, @Param("email") String email, @Param("userId") String userId);

    @Query("SELECT m FROM Member m WHERE m.userId = :userId")
    Optional<Member> findByUserId(@Param("userId") String userId);
}
