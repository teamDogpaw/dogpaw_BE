package com.project.dogfaw.bookmark.controller;


import com.project.dogfaw.bookmark.service.BookMarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class BookMarkController {

    private final BookMarkService bookMarkService;

    /*userId는 RequestBody로 받아와서 사용, 만약 불가능하면 security contextHolder에서 유저네임 꺼내와 userRepository에서 Id 조회해서 사용하기*/
    @PostMapping("/api/bookMark/{postId}")
    public boolean bookMark(@PathVariable Long postId, @RequestBody Long userId){
        if(bookMarkService.bookMarkUp(postId, userId)){
            return true;
        } else return false;
    }

}
