package com.project.dogfaw.mypage.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.project.dogfaw.common.exception.CustomException;
import com.project.dogfaw.common.exception.ErrorCode;
import com.project.dogfaw.mypage.dto.MypageRequestDto;
import com.project.dogfaw.user.dto.StackDto;
import com.project.dogfaw.user.model.Stack;
import com.project.dogfaw.user.model.User;
import com.project.dogfaw.user.repository.StackRepository;
import com.project.dogfaw.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3Uploader {
    private final AmazonS3Client amazonS3Client;
    private final UserRepository userRepository;
    private final StackRepository stackRepository;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadFiles(MultipartFile multipartFile, String dirName, MypageRequestDto requestDto, User user) throws IOException {
        File uploadFile = convert(multipartFile)  // 파일 변환할 수 없으면 에러
                .orElseThrow(() -> new IllegalArgumentException("error: MultipartFile -> File convert fail"));
        return upload(uploadFile, dirName, requestDto ,user);
    }

    public String upload(File uploadFile, String filePath, MypageRequestDto requestDto, User user) {
        /*닉네임 중복검사 후 S3업로드 및 편집*/
        String nickname = requestDto.getNickname();
        /*현재 사용하고 있는 닉네임은 사용가능*/
        if(!user.getNickname().equals(nickname)){
            if (userRepository.existsByNickname(nickname)) {
                throw new CustomException(ErrorCode.SIGNUP_NICKNAME_DUPLICATE_CHECK);
            }
        }

        if(user.getProfileImg()!=null) {
            String imgKey = user.getImgkey();
            amazonS3Client.deleteObject(bucket,imgKey);
        }
        String fileName = filePath + "/" + UUID.randomUUID() + uploadFile.getName();   // S3에 저장된 파일 이름
        String uploadImageUrl = putS3(uploadFile, fileName); // s3로 업로드
        String imgKey = fileName; //파일 확장자 fileName 따로저장(이유: getURL에서 한글이름 파일 깨짐=삭제불가)
        removeNewFile(uploadFile);

        return updateUser(uploadImageUrl,imgKey,requestDto,user);
    }

    /*유저정보 업데이트(프로필추가 및 닉네임 편집)*/
    @Transactional
    public String updateUser(String uploadImageUrl,String imgKey, MypageRequestDto requestDto, User user) {

            Long userId = user.getId();
            user.setNickname(requestDto.getNickname());
            user.setProfileImg(uploadImageUrl);
            user.setImgkey(imgKey);
            stackRepository.deleteAllByUserId(userId);
            List<Stack> stack = stackRepository.saveAll(tostackByUserId(requestDto.getStacks(),user));
            user.updateStack(stack);
            return uploadImageUrl;
            
        }

    // S3로 업로드
    //유저의 기존 프로필 이미지 S3에서 삭제 후 업로드
    private String putS3(File uploadFile, String fileName) {
        try {
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        }catch (Exception e){
            throw new CustomException(ErrorCode.IMAGE_UPLOAD_ERROR);
        }
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // 로컬에 저장된 이미지 지우기
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            System.out.println("File delete success");
            return;
        }
        System.out.println("File delete fail");
    }

    // 로컬에 파일 업로드 하기
    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(System.getProperty("user.dir") + "/" + file.getOriginalFilename());
        if (convertFile.createNewFile()) { // 바로 위에서 지정한 경로에 File이 생성됨 (경로가 잘못되었다면 생성 불가능)
            try (FileOutputStream fos = new FileOutputStream(convertFile)) { // FileOutputStream 데이터를 파일에 바이트 스트림으로 저장하기 위함
                fos.write(file.getBytes());
            }catch (Exception e){
                throw new CustomException(ErrorCode.WRONG_IMAGE_FORMAT);
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

    private List<Stack> tostackByUserId(List<StackDto> requestDto, User user) {
        List<Stack> stackList = new ArrayList<>();
        for(StackDto stackdto : requestDto){
            stackList.add(new Stack(stackdto, user));
        }
        return stackList;
    }
}


