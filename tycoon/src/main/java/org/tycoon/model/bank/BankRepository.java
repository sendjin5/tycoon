package org.tycoon.model.bank;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tycoon.model.bank.entity.Bank;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {

    // ✅ 1. 모든 대출 내역 조회
    @Query("SELECT b FROM Bank b")
    List<Bank> getAllLoans();

    // ✅ 2. 특정 대출 조회 (loanNo 기준)
    Optional<Bank> findByLoanNo(Long loanNo);

    // ✅ 3. 대출 금액 업데이트
    @Modifying
    @Query("UPDATE Bank b SET b.loanMoney = :loanMoney WHERE b.loanNo = :loanNo")
    void updateLoanAmount(@Param("loanNo") Long loanNo, @Param("loanMoney") int loanMoney);

    // ✅ 4. 게임 번호로 대출 내역 조회 (loanType 포함)
    @Query(value = """
        SELECT 
            loan_no AS loanNo,
            loandate AS loandate,
            loanMoney AS loanMoney,
            CASE
                WHEN loanMoney = 1000000 AND loan_no = (
                    SELECT MIN(loan_no)
                    FROM bank
                    WHERE play_no = :gameNo AND loanMoney = 1000000
                )
                THEN '초기 운영 대출금'
                ELSE '중간 운영 대출금'
            END AS loanType
        FROM bank
        WHERE play_no = :gameNo
        ORDER BY
            CASE
                WHEN loanMoney = 1000000 AND loan_no = (
                    SELECT MIN(loan_no)
                    FROM bank
                    WHERE play_no = :gameNo AND loanMoney = 1000000
                )
                THEN 1 ELSE 0 END ASC,
            loandate DESC,
            loan_no DESC
        """, nativeQuery = true)
    List<Map<String, Object>> getLoansByGameNo(@Param("gameNo") String gameNo);

    // ✅ 5. 대출 신청 (INSERT) → save() 또는 NativeQuery 사용 가능
    @Modifying
    @Query(value = """
        INSERT INTO BANK (
            LOAN_NO, LOANDATE, LOANMONEY, MEMBER_NO, PLAY_NO, USER_ID
        ) VALUES (
            loan_seq.nextval,
            SYSDATE,
            :loanMoney,
            (SELECT MEMBERNO FROM MEMBER WHERE USERID = (SELECT USER_ID FROM GAME WHERE PLAY_NO = :gameNo)),
            :gameNo,
            (SELECT USER_ID FROM GAME WHERE PLAY_NO = :gameNo)
        )
        """, nativeQuery = true)
    void insertLoan(@Param("loanMoney") int loanMoney, @Param("gameNo") String gameNo);

    // ✅ 6. 게임의 loan/cash 업데이트
    @Modifying
    @Query("""
        UPDATE Game g
        SET g.cash = g.cash + :loanMoney,
            g.loan = g.loan + :loanMoney
        WHERE g.playNo = :gameNo
    """)
    void updateGameLoan(@Param("loanMoney") int loanMoney, @Param("gameNo") String gameNo);

    // ✅ 7. 게임 날짜 조회
    @Query("SELECT MAX(g.playDay) FROM Game g WHERE g.playNo = :gameNo")
    Integer getPlaydayByGameNo(@Param("gameNo") String gameNo);

    // ✅ 8. 일별 수익 리포트
    @Query(value = """
        SELECT 
            r.salesday AS saleDay,
            r.salesmount AS 판매수익,
            CASE WHEN r.qeezeyn = 'Y' THEN r.salesmount * 0.05 ELSE 0 END AS 퀴즈혜택,
            r.salesmount * 0.2 AS 폐기수익,
            (r.salesmount + 
             CASE WHEN r.qeezeyn = 'Y' THEN r.salesmount * 0.05 ELSE 0 END + 
             r.salesmount * 0.2) AS 총수입,
            SUM(s.orderquantity * g.orderprice) AS 발주비용,
            20000 AS 운영비,
            (SUM(s.orderquantity * g.orderprice) + 20000) AS 총지출,
            ((r.salesmount + 
              CASE WHEN r.qeezeyn = 'Y' THEN r.salesmount * 0.05 ELSE 0 END + 
              r.salesmount * 0.2) - 
              (SUM(s.orderquantity * g.orderprice) + 20000)) AS 총계,
            MAX(gm.cash) AS 잔고
        FROM revenue r
        JOIN game gm ON r.play_no = gm.play_no
        LEFT JOIN storage s ON r.play_no = s.playno
        LEFT JOIN goods g ON s.goodsno = g.goodsno
        WHERE gm.play_no = :gameNo
          AND r.salesday = :selectedDay
        GROUP BY r.salesday, r.salesmount, r.qeezeyn
        """, nativeQuery = true)
    Map<String, Object> getDailyRevenue(@Param("gameNo") String gameNo, @Param("selectedDay") String selectedDay);

    // ✅ 9. 게임의 현재 잔액 조회
    @Query("SELECT g.cash FROM Game g WHERE g.playNo = :gameNo")
    Integer getGameCash(@Param("gameNo") String gameNo);
}
