package org.tycoon.model.maingame;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tycoon.model.maingame.entity.Game;
import org.tycoon.model.maingame.entity.Product;
import org.tycoon.model.maingame.entity.Revenue;

import java.util.List;

@Repository
public interface MainGameRepository extends JpaRepository<Game, Long> {

    // 1. 게임별 상품 목록 조회
    @Query("""
        SELECT new com.bs.spring.maingame.model.dto.Product(
            s.orderingNo, s.goodsNo, s.playNo, g.goodType, g.goodsName,
            g.image, s.expDate, s.orderQuantity, s.salePrice, s.saleDgree, s.disposalYN
        )
        FROM Storage s
        JOIN Goods g ON s.goodsNo = g.goodsNo
        WHERE s.playNo = :gameNo
    """)
    List<Product> getAllProductByGameNo(@Param("gameNo") int gameNo);

    // 2. 게임 플레이 번호 (최신 게임 코드) 조회
    @Query("SELECT MAX(g.playNo) FROM Game g WHERE g.userId = :userId")
    Integer getNewGameCode(@Param("userId") String userId);

    // 3. 게임 보유 금액 조회
    @Query("SELECT g.cash FROM Game g WHERE g.playNo = :gameNo")
    Integer getMoneyData(@Param("gameNo") int gameNo);

    // 4. 마지막 게임 날짜 조회
    @Query("""
        SELECT g.playDay FROM Game g
        WHERE g.userId = :userId
        ORDER BY g.playNo DESC
    """)
    List<Integer> getGamePlayDay(@Param("userId") String userId); // LIMIT 1은 Java에서 처리

    // 5. 게임 금액/대출 정보 조회
    @Query("SELECT g FROM Game g WHERE g.playNo = :gameNo")
    Game getCashAndLoan(@Param("gameNo") int gameNo);

    // 6. 퀴즈 여부/폐기 가격 조회
    @Query("SELECT r FROM Revenue r WHERE r.playNo = :gameNo")
    Revenue getQuizAndDispose(@Param("gameNo") int gameNo);

    // 7. 게임 금액 증가
    @Modifying
    @Query("UPDATE Game g SET g.cash = g.cash + :price WHERE g.playNo = :gameNo")
    void moneyExpense(@Param("price") int price, @Param("gameNo") int gameNo);

    // 8. 게임 금액 변경
    @Modifying
    @Query("UPDATE Game g SET g.cash = :price WHERE g.playNo = :gameNo")
    void moneyChange(@Param("price") int price, @Param("gameNo") int gameNo);

    // 9. 날짜 변경
    @Modifying
    @Query("UPDATE Game g SET g.playDay = :salesDay WHERE g.playNo = :gameNo")
    void daysChange(@Param("salesDay") int salesDay, @Param("gameNo") int gameNo);
}
