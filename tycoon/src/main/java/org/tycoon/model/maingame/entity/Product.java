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
public class Product {
    @Id
    private Integer orderingNo;
    private Integer goodsNo;
    private Integer playNo;
    private String goodType;
    private String goodsName;
    private String image;
    private Integer expDate;
    private Integer orderQuantity;
    private Integer salePrice;
    private Integer saleDgree;
    private String disposalYN;
}
