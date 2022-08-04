package com.project.dogfaw.post.service;


import com.amazonaws.services.quicksight.model.UserRole;
import com.project.dogfaw.acceptance.repository.AcceptanceRepository;
import com.project.dogfaw.apply.model.UserApplication;
import com.project.dogfaw.apply.repository.UserApplicationRepository;
import com.project.dogfaw.bookmark.model.BookMark;
import com.project.dogfaw.bookmark.repository.BookMarkRepository;
import com.project.dogfaw.comment.repository.CommentRepository;
import com.project.dogfaw.common.exception.CustomException;
import com.project.dogfaw.common.exception.ErrorCode;
import com.project.dogfaw.common.exception.StatusResponseDto;
import com.project.dogfaw.post.dto.PostDetailResponseDto;
import com.project.dogfaw.post.dto.PostRequestDto;
import com.project.dogfaw.post.dto.MyApplyingResponseDto;
import com.project.dogfaw.post.model.Post;
import com.project.dogfaw.post.model.PostStack;
import com.project.dogfaw.post.model.UserStatus;
import com.project.dogfaw.post.repository.PostRepository;
import com.project.dogfaw.post.repository.PostStackRepository;
import com.project.dogfaw.user.model.User;
import com.project.dogfaw.user.model.UserRoleEnum;
import com.project.dogfaw.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostStackRepository postStackRepository;
    private final BookMarkRepository bookMarkRepository;
    private final UserApplicationRepository userApplicationRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final AcceptanceRepository acceptanceRepository;



    //전체조회
    public Map<String, Object> allPost(User user, long page) {
        try {
            PageRequest pageRequest = PageRequest.of((int) page, 24);
            //BookmarkStatus를 true || false를 담아주기 위해 for문을 두번 돌리기 때문에
            //모든 게시글을 다 찾아와서 for문을 돌렸을 때 서버 부하가 어느정도 올지 모르겠음(만약 게시글이 많을 경우)
            //게시글이 많을 경우 이중 for문때문에 부하가 상당할 것으로 예상이 되므로 메인페이지에서 최신 게시글 Top20정도만
            //보여주는 것도 하나의 방법이 될 것 같음.

            //내림차순으로 Top 20만 내림차순으로
            //List<Post> posts = postRepository.findTop20ByOrderByModifiedAtDesc();
            //모든 게시글 내림차순으로(startAt으로 변경 필요)
            Slice<Post> posts = postRepository.findAllByOrderByCreatedAtDesc(pageRequest);

            //BookMarkStatus를 추가적으로 담아줄 ArrayList 생성
            ArrayList<MyApplyingResponseDto> postList = new ArrayList<>();
            //true || false 값을 담아줄 Boolean type의 bookMarkStatus 변수를 하나 생성
            Boolean bookMarkStatus = false;

            //로그인한 유저가 아닐때는 모든게시글을 불러와주고 bookMarkStatus는 모두 false로 리턴
            if (user == null) {
                for (Post post : posts) {
                    Long postId = post.getId();
                    User writer = post.getUser();
                    //Stacks(기술스택) 가져오기
                    List<PostStack> postStacks = postStackRepository.findByPostId(postId);
                    List<String> stringPostStacks = new ArrayList<>();
                    for (PostStack postStack : postStacks) {
                        stringPostStacks.add(postStack.getStack());
                    }
                    //PostResponseDto를 이용해 게시글과, 북마크 상태,writer 는 해당 게시글 유저의 프로필 이미지를 불러오기 위함
                    MyApplyingResponseDto mainDTO = new MyApplyingResponseDto(post, stringPostStacks, bookMarkStatus, writer);
                    //아까 생성한 ArrayList에 새로운 모양의 값을 담아줌
                    postList.add(mainDTO);
                }
            } else {
                //유저가 북마크한 것을 리스트로 모두 불러옴
                List<BookMark> userPosts = bookMarkRepository.findAllByUser(user);
                //유저가 북마크한 게시글을 찾아 리스트에 담아주기 위해 ArrayList 생성
                ArrayList<Post> userPostings = new ArrayList<>();
                for (BookMark userPost : userPosts) {
                    Post userPosting = userPost.getPost();
                    userPostings.add(userPosting);
                }
                //일치하면 bookMarkStatus = true 아니면 false를 bookMarkStatus에 담아줌
                for (Post post : posts) {
                    Long postId = post.getId();
                    User writer = post.getUser();
                    for (Post userPost : userPostings) {
                        Long userPostId = userPost.getId();
                        //객체대 객체일경우 if문으로 동일 비교 불가?
                        //객체를 불러올경우 메모리에 할당되는 주소값으로 불려지기 때문에 비교시 다를 수 밖에 없음
                        //객체 안의 특정 타입 id, nickname 등으로 비교하여 문제 해결
                        // 객체 안에있는 특정 데이터 타입으로 비교해줘야 함
                        if (postId.equals(userPostId)) {
                            bookMarkStatus = true;
                            break; //true일 경우 탈출
                        } else {
                            bookMarkStatus = false;
                        }
                    }
                    List<PostStack> postStacks = postStackRepository.findByPostId(postId);
                    List<String> stringPostStacks = new ArrayList<>();
                    for (PostStack postStack : postStacks) {
                        stringPostStacks.add(postStack.getStack());
                    }

                    //PostResponseDto를 이용해 게시글과, 북마크 상태,writer 는 해당 게시글 유저의 프로필 이미지를 불러오기 위함
                    MyApplyingResponseDto mainDto = new MyApplyingResponseDto(post, stringPostStacks, bookMarkStatus, writer);
                    //아까 생성한 ArrayList에 새로운 모양의 값을 담아줌
                    postList.add(mainDto);
                }
            }
            Map<String, Object> data = new HashMap<>();
            data.put("postList", postList);
            data.put("isLast", posts.isLast());
            return data;
        }catch (Exception e){
         throw new CustomException(ErrorCode.POST_ALL_LOAD_FAIL);
        }
    }

    // post 등록
    @Transactional
    public MyApplyingResponseDto postPost(PostRequestDto postRequestDto, User user) {
        // 게시글 작성자 저장 (편의 메서드 -> user에도 post에 해당 post add)
        Post post = postRepository.save(new Post(postRequestDto, user));
//        PostResponseDto postResponseDto = new PostResponseDto(post,false, user);
        // 저장된 Post -> PostResponseDto에 담아 리턴
//        return postResponseDto;

        List<String> stacks = postRequestDto.getStacks();
        for(String stack : stacks){
            PostStack postStack = new PostStack(post.getId(), stack);
            postStackRepository.save(postStack);

        }

        return new MyApplyingResponseDto(post, stacks, false, user);

    }


    //post 상세조회


    public PostDetailResponseDto getPostDetail(Long postId,User user) {
        //게시글 존재유무
        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new CustomException(ErrorCode.POST_NOT_FOUND)
        );

        //신청자 수
        List<UserApplication> applicationList =post.getUserApplications();
        int applierCnt = applicationList.size();

        /*로그인안한유저와 로그인한 유저구분*/
        if (user == null) {
            String userStatus = UserStatus.USER_STATUS_ANONYMOUS.getUserStatus();
            //로그인하지 않은 유저는 bookmarkStatus 항상 false
            //북마크유무
            Boolean bookMarkStatus = false;
            List<PostStack> postStacks = postStackRepository.findByPostId(postId);
            List<String> stringPostStacks = new ArrayList<>();
            for(PostStack postStack : postStacks){
                stringPostStacks.add(postStack.getStack());
            }
            return new PostDetailResponseDto(post, stringPostStacks, user, bookMarkStatus,applierCnt,userStatus);
        }else {

        /*
        1.모집글을 쓴 유저와 로그인한 유저가 같을 경우 : userStatus = author
        2.로그인한 유저가 수락된 유저일 경우 : userStatus = participant
        3.로그인한 유저가 지원한 유저일 경우 : userStatus = applicant
        4.로그인한 유저가 지원하지 않은 유저일 경우 : userStatus = member
        */
            //userStatus 판별
            String checkName = user.getUsername();
            String nickname = post.getUser().getUsername(); // 해당 게시글 작성자 닉네임

            //지원유무
            Boolean applyingStatus = userApplicationRepository.existsByUserAndPost(user, post);
            //참여수락유무
            Boolean acceptedStatus = acceptanceRepository.existsByUserAndPost(user, post);

            String userStatus;
            if (user.getRole()==UserRoleEnum.ADMIN){
                userStatus = UserStatus.USER_STATUS_MASTER.getUserStatus();
            } else if (checkName.equals(nickname)) {
                userStatus = UserStatus.USER_STATUS_AUTHOR.getUserStatus();
            } else if (Boolean.TRUE.equals(acceptedStatus)) {
                userStatus = UserStatus.USER_STATUS_PARTICIPANT.getUserStatus();
            } else if (Boolean.TRUE.equals(applyingStatus)) {
                userStatus = UserStatus.USER_STATUS_APPLICANT.getUserStatus();
            } else {
                userStatus = UserStatus.USER_STATUS_MEMBER.getUserStatus();
            }

            //북마크유무
            Boolean bookMarkStatus = bookMarkRepository.existsByUserAndPost(user, post);

            List<PostStack> postStacks = postStackRepository.findByPostId(postId);
            List<String> stringPostStacks = new ArrayList<>();
            for (PostStack postStack : postStacks) {
                stringPostStacks.add(postStack.getStack());
            }

            return new PostDetailResponseDto(post, stringPostStacks, user, bookMarkStatus, applierCnt, userStatus);
        }
    }

    //게시글 수정
    @Transactional
    public void updatePost(Long postId, PostRequestDto postRequestDto, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new CustomException(ErrorCode.POST_NOT_FOUND)
        );
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->new CustomException(ErrorCode.NOT_MATCH_USER_INFO)
        );
        if (!Objects.equals(username, post.getUser().getUsername())){
            throw new CustomException(ErrorCode.POST_UPDATE_WRONG_ACCESS);
        }
        postStackRepository.deleteByPostId(postId);
        for (String stack : postRequestDto.getStacks()){
            postStackRepository.save(new PostStack(postId,stack));
        }

        post.update(postRequestDto, user.getId());
    }

    //게시글 삭제
    @Transactional
    public void deletePost(Long postId, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new CustomException(ErrorCode.POST_NOT_FOUND)
        );
        if(user.getUsername().equals(post.getUser().getUsername()) || user.getRole()== UserRoleEnum.ADMIN ){
            userApplicationRepository.deleteAllByPost(post);
            acceptanceRepository.deleteAllByPost(post);
            bookMarkRepository.deleteAllByPost(post);
            commentRepository.deleteAllByPost(post);
            postRepository.deleteById(postId);
        } else {
            throw new CustomException(ErrorCode.POST_INQUIRY_NO_AUTHORITY);
        }

    }

//    private List<PostStack> tostackByPostId(List<StackDto> requestDto, Post post) {
//        List<PostStack> stackList = new ArrayList<>();
//        for(StackDto stackdto : requestDto){
//            stackList.add(new PostStack(stackdto, post ));
//        }
//        return stackList;
//    }



    //북마크 랭킹
    public ArrayList<MyApplyingResponseDto> bookMarkRank(User user) {
        try {
            PageRequest pageRequest = PageRequest.of(0, 3);

            List<Post> posts = postRepository.findByOrderByBookmarkCntDesc(pageRequest);
            ArrayList<MyApplyingResponseDto> postList = new ArrayList<>();

            Boolean bookMarkStatus = false;

            if (user == null) {
                for (Post post : posts) {
                    Long postId = post.getId();
                    User writer = post.getUser();
                    //다솔다솔이(민지민지) 추가한 부분
                    List<PostStack> postStacks = postStackRepository.findByPostId(postId);
                    List<String> stringPostStacks = new ArrayList<>();
                    for (PostStack postStack : postStacks) {
                        stringPostStacks.add(postStack.getStack());
                    }
                    MyApplyingResponseDto postDto = new MyApplyingResponseDto(post, stringPostStacks, bookMarkStatus, writer);
                    postList.add(postDto);
                }
            } else {
                List<BookMark> userPosts = bookMarkRepository.findAllByUser(user);
                ArrayList<Post> userPostings = new ArrayList<>();
                for (BookMark userPost : userPosts) {
                    Post userPosting = userPost.getPost();
                    userPostings.add(userPosting);
                }

                for (Post post : posts) {
                    Long postId = post.getId();
                    User writer = post.getUser();
                    for (Post userPost : userPostings) {
                        Long userPostId = userPost.getId();

                        if (postId.equals(userPostId)) {
                            bookMarkStatus = true;
                            break;
                        } else {
                            bookMarkStatus = false;
                        }
                    }
                    List<PostStack> postStacks = postStackRepository.findByPostId(postId);
                    List<String> stringPostStacks = new ArrayList<>();
                    for (PostStack postStack : postStacks) {
                        stringPostStacks.add(postStack.getStack());
                    }
                    MyApplyingResponseDto postDto = new MyApplyingResponseDto(post, stringPostStacks, bookMarkStatus, writer);
                    postList.add(postDto);
                }
            }
            return postList;
        }catch (Exception e){
            throw new CustomException(ErrorCode.POST_RANK_LOAD_FAIL);
        }
    }
    @Transactional
    /*모집마감,모집마감 취소(작성자만)*/
    public ResponseEntity<Object> updateDeadline(Long postId, User user) {
        //게시글
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        //모집글 작성자 확인
        if (!user.getNickname().equals(post.getUser().getNickname())) {
            throw new CustomException(ErrorCode.POST_INQUIRY_NO_AUTHORITY);
        }
        if (post.getDeadline() == false) {
            Boolean deadline = true;
            post.updateDeadline(deadline);
            return new ResponseEntity(new StatusResponseDto("모집이 마감되었습니다",true), HttpStatus.OK);
        } else {
            //만약 모집인원수가 모두 찼을 경우 모집취소 불가
            if (post.getCurrentMember() >= post.getMaxCapacity()) {
                throw new CustomException(ErrorCode.POST_PEOPLE_SET_CLOSED);
            }
            Boolean deadline = false;
            post.updateDeadline(deadline);
            return new ResponseEntity(new StatusResponseDto("모집 마감이 취소되었습니다",false), HttpStatus.OK);
        }
    }
}



