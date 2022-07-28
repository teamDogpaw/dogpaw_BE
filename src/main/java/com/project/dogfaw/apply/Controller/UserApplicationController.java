package com.project.dogfaw.apply.Controller;

import com.project.dogfaw.apply.service.UserApplicationService;
import com.project.dogfaw.common.CommonService;
import com.project.dogfaw.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserApplicationController {

    private final UserApplicationService userApplicationService;
    private final CommonService commonService;

    @PostMapping("/api/apply/{postId}")
    public ResponseEntity<Object> userApply(@PathVariable Long postId){
        User user = commonService.getUser(); 
 
        return userApplicationService.userApply(postId,user);

    }

}
