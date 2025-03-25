package org.tycoon.model.member.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
public class Member{
    @Id
    @GeneratedValue()
    private Integer no;
    private String userId;
    private String password;
    private String nick;
    private String email;

}
