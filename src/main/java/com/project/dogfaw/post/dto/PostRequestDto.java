package com.project.dogfaw.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostRequestDto {

    private String title;
    private String online;

    private List<String> stacks;
    private String period;
    private String startAt;
    private int maxCapacity;
    private String content;

    private String profileImg; 


    public PostRequestDto(String title, String online, String period, String startAt, int maxCapacity, String content) {

        this.title = title;
        this.online = online;
//        this.stacks = stacks;
        this.period = period;
        this.startAt = startAt;
        this.maxCapacity = maxCapacity;
        this.content = content;
    }
}

//“title”:  프로젝트제목,
//“online”: 진행방식,
//“stack”: 기술스택,
//“period”: 예상 진행기간,
//“startAt”: 시작 예정일,
//“capacity” : 모집인원,
//“content”: 프로젝트소개