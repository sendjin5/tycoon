package org.tycoon.model.maingame.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Goods {
    @Id
    private Integer goodno;
    private Integer orderavailable;
    private Integer typeexpdate;
    private Integer orderprice;
    private String goodstype;
    private String goodname;
}
