package com.project.dogfaw.user.model;

import com.project.dogfaw.user.dto.SignupRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity(name = "Users")
@DynamicUpdate // null 값인 field를 DB에서 설정된 default을 줌
public class User {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
//    @JsonIgnore
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column
    private String profileImg;

    @Column
    private Long kakaoId;

//    @Column
//    private String stack;
    @OneToMany
    @JoinColumn(name = "stack_id")
    private List<Stack> stacks = new ArrayList<>();


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
