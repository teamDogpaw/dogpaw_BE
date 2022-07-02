package com.project.dogfaw.bookmark.controller;


import com.project.dogfaw.bookmark.service.BookMarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class BookMarkController {

    private final BookMarkService bookMarkService;

    @PostMapping("/api/bookMark/{postId}/{userId}")
    public boolean bookMark(@PathVariable Long postId,@PathVariable Long userId){
        if(bookMarkService.bookMarkUp(postId, userId)){
            return true;
        } else return false;
    }

}
