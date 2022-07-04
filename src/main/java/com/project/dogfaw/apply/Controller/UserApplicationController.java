package com.project.dogfaw.apply.Controller;

import com.project.dogfaw.apply.service.UserApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserApplicationController {

    private final UserApplicationService userApplicationService;

    @PostMapping("/api/apply/{postId}/{userId}")
    public boolean userApply(@PathVariable Long postId, @PathVariable Long userId){
        userApplicationService.userApply(postId,userId)
    }

}
