package com.project.dogfaw.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.dogfaw.user.dto.SignupRequestDto;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "Users")
@DynamicUpdate // null 값인 field를 DB에서 설정된 default을 줌
public class User {
 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

//    @Column(nullable = false)
    @Column
//    @JsonIgnore
    private String password;

//    @Column(nullable = false, unique = true)
    @Column
    private String nickname;

    @Column
    private String profileImg;

    //S3삭제시 사용
    @JsonIgnore
    @Column
    private String Imgkey;

    @Column
    private Long kakaoId;

//    @Column
//    private String googleId;

//    @Column
//    private String googleId;

    @OneToMany
    @JoinColumn(name = "stack_id")
    private List<Stack> stacks = new ArrayList<>();

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;


    public void updateStack(List<Stack> stack) {
        this.stacks = stack;
    }

    public void addInfo(SignupRequestDto requestDto) {
        this.nickname = requestDto.getNickname();
    }



//    private String List<bookMark>;
//
//    private String List<userApplication>



}
