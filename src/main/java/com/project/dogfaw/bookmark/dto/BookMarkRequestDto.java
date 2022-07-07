package com.project.dogfaw.bookmark.dto;

import lombok.Getter;

@Getter
public class BookMarkRequestDto {

    private Long userId;


    public BookMarkRequestDto(Long userId) {
        this.userId = userId;
    }
}
