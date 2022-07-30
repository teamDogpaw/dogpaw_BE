package com.project.dogfaw.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.dogfaw.user.dto.SignupRequestDto;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
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

    @Column
    private String password;

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

    @OneToMany
    @JoinColumn(name = "stack_id")
    private List<Stack> stacks;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;


    public void updateStack(List<Stack> stack) {
        this.stacks = stack;
    }

    public void updateNickname(SignupRequestDto requestDto) {
        this.nickname = requestDto.getNickname();
    }

    public void basicImg(){
        this.profileImg = null;
        this.Imgkey = null;
    }

//    private String List<bookMark>;
//
//    private String List<userApplication>



}
