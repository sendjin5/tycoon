package org.tycoon.model.maingame.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ordering {
    //이것은 발주 기능 dto입니다.
    private Integer goodsno;
    private Integer orderprice;
    private String image;
    private Integer orderquantity;
    private Integer expdate;
    private String goodsname;
    private String goodstype;
    private Integer storagelevel;
    private Integer amount;
    private Integer playday;
    private Integer storagecount;
}
