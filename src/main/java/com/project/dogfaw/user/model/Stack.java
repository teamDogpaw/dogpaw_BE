package com.project.dogfaw.user.model;

import com.project.dogfaw.user.dto.StackDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@DynamicUpdate // null 값인 field 를 DB에서 설정된 default을 줌
public class Stack {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column
    private String stack;

    private Long userId;

    public Stack(StackDto stackdto) {
        this.stack = stackdto.getStack();
    }

    public Stack(StackDto stackdto, User user) {
        this.stack = stackdto.getStack();
        this.userId = user.getId();
    }
}
