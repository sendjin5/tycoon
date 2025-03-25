package org.tycoon.model.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tycoon.model.storage.entity.Storage;

@Repository
public interface StorageRepository extends JpaRepository<Storage, Long> {

    // 1. selectAllPrd – 상품 그룹 요약 조회 (JPQL로 완전 동일하게 구현은 어려움 → native query 권장)
    @Query(value = """
        SELECT new com.bs.spring.storage.dto.StoreSummary(
            s.goodsNo,
            g.goodsType,
            g.goodsName,
            CASE WHEN s.expDate = 1 THEN g.deImage ELSE g.image END,
            SUM(s.orderQuantity),
            CASE WHEN s.expDate = 1 THEN 0 ELSE 1 END
        )
        FROM Storage s
        JOIN Goods g ON s.goodsNo = g.goodsNo
        WHERE s.playNo = :gameNo
        GROUP BY s.goodsNo, g.goodsType, g.goodsName,
                 CASE WHEN s.expDate = 1 THEN g.deImage ELSE g.image END,
                 CASE WHEN s.expDate = 1 THEN 0 ELSE 1 END
    """)
    List<StoreSummary> selectAllPrd(@Param("gameNo") int gameNo);


    // 1. 상품 주문 목록 요약 (selectAllPrd)
    @Query("""
        SELECT new com.bs.spring.ordering.dto.OrderingSummary(
            g.goodsNo,
            g.goodsName,
            SUM(COALESCE(s.orderQuantity, 0)),
            g.orderPrice,
            g.goodsType,
            g.image
        )
        FROM Goods g
        LEFT JOIN Storage s ON g.goodsNo = s.goodsNo AND s.playNo = :gameNo
        WHERE g.image NOT LIKE %:level%
        GROUP BY g.goodsNo, g.goodsName, g.orderPrice, g.goodsType, g.image
        ORDER BY g.orderPrice
    """)
    List<OrderingSummary> selectAllPrd(@Param("gameNo") int gameNo, @Param("level") String level); // 예: level = "50"

    // 2. 게임 정보 조회 (selectGameInfo)
    @Query("""
        SELECT new com.bs.spring.ordering.dto.OrderingInfo(
            CASE
                WHEN g.storageLevel = 1 THEN 50
                WHEN g.storageLevel = 2 THEN 70
                WHEN g.storageLevel = 3 THEN 90
                WHEN g.storageLevel = 4 THEN 110
                WHEN g.storageLevel = 5 THEN 130
                ELSE 150
            END,
            g.cash,
            g.playDay
        )
        FROM Game g
        WHERE g.playNo = :gameNo
    """)
    OrderingInfo selectGameInfo(@Param("gameNo") int gameNo);
}
