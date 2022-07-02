package com.project.dogfaw.post.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PostRequestDto {

    private String title;
    private Boolean online;
    private String stack;
    private String period;
    private int startAt;
    private int capacity;
    private String content;

    public PostRequestDto(String title, Boolean online, String stack, String period, int startAt, int capacity, String content){
        this.title = title;
        this.online = online;
        this.stack = stack;
        this.period = period;
        this.startAt = startAt;
        this.capacity = capacity;
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