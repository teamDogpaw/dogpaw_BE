package com.project.dogfaw.common.validator;

import com.project.dogfaw.common.exception.CustomException;
import com.project.dogfaw.common.exception.ErrorCode;
import com.project.dogfaw.post.model.Post;

public class ApplyValidator {

    public static void currentMemberCheck(Post post) {
        //해당 게시물의 현재 모집인원 수 와 쵀대 모집인원 수
        int currentCnt = post.getCurrentMember();
        int maxCnt = post.getMaxCapacity();

        //인원 꽉찰 경우 모집마감(deadline = true)
        if(currentCnt==maxCnt){
            post.isDeadline();
            throw new CustomException(ErrorCode.APPLY_PEOPLE_SET_CLOSED);
        }
    }
}
