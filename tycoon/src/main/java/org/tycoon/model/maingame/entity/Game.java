package org.tycoon.model.maingame.entity;

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
public class Game {
    @Id
    private Integer playNo;
    private Integer memberNo;
    private String userId;
    private Integer cash;
    private Integer loan;
    private Integer playDay;
    private Integer storageLevel;
    private Integer state;
    private Integer reliability;
}
