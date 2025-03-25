package org.tycoon.model.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserData {
    private String userId;
    private String nickname;
    private String email;
    private int cash;
    private int loan;
    private int storageLevel;
    private int playNo;
    private Long totalRevenue;
}