package com.project.dogfaw.bookmark.controller;


import com.project.dogfaw.bookmark.dto.BookMarkRequestDto;
import com.project.dogfaw.bookmark.service.BookMarkService;
import com.project.dogfaw.common.CommonService;
import com.project.dogfaw.post.model.Post;
import com.project.dogfaw.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BookMarkController {

    private final CommonService commonService;
    private final BookMarkService bookMarkService;

    /*북마크하기*/
    @PostMapping("/api/bookMark/{postId}")
    public boolean bookMark(@PathVariable Long postId){
        User user = commonService.getUser();

        if(bookMarkService.bookMarkUp(postId, user)){
            return true;
        } else return false;
    }
}
