package org.tycoon.model.storage.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Storage {
    @Id
    private Integer orderingno;
    private Integer goodsno;
    private Integer playno;
    private Integer expdate;
    private Integer orderquantity;
    private Integer saleprice;
    private Integer saledgree;
    private String  disposalyn;
    private Integer expyn;
    private Integer cash;
}


