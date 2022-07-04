package com.project.dogfaw.apply.Controller;

import com.project.dogfaw.apply.service.UserApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserApplicationController {

    private final UserApplicationService userApplicationService;


    @PostMapping("/api/apply/{postId}")
    public Long userApply(@PathVariable Long postId, @RequestBody Long userId){
        //모집인원 초과시 return 1L, 참여등록되었을 시 return 2L, 참여취소 시 return 3L
        if (userApplicationService.userApply(postId,userId)==1){
            return 1L;
        } else if (userApplicationService.userApply(postId,userId)==2){
            return 2L;
        } else {
            return 3L;
        }


    }

}
