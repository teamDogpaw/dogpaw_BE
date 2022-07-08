//package com.project.dogfaw.mypage.img;
//
//import com.amazonaws.services.s3.AmazonS3Client;
//import com.amazonaws.services.s3.model.CannedAccessControlList;
//import com.amazonaws.services.s3.model.PutObjectRequest;
//import com.project.dogfaw.common.exception.CustomException;
//import com.project.dogfaw.common.exception.ErrorCode;
//import com.project.dogfaw.mypage.dto.MypageRequestDto;
//import com.project.dogfaw.user.model.User;
//import com.project.dogfaw.user.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.web.multipart.MultipartFile;
//
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.Optional;
//
//@Slf4j
//@RequiredArgsConstructor
//@Component
//public class S3Uploader {
//
//    private final UserRepository userRepository;
//    private final AmazonS3Client amazonS3Client;
//
//    @Value("${cloud.aws.s3.bucket}")
//    private String bucket; //S3 버킷 이름
//
//    public String upload(MultipartFile multipartFile, String dirNam, User user, MypageRequestDto requestDto) throws IOException {
//        File uploadFile = convert(multipartFile)
//                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."));
//
//        return upload(uploadFile, dirNam, user, requestDto);
//    }
//
//
//    private String upload(File uploadFile, String dirName, User user, MypageRequestDto requestDto) {
//        String fileName = dirName + "/" + uploadFile.getName();
//        String uploadImageUrl = putS3(uploadFile, fileName);
//        removeNewFile(uploadFile);
//        //닉네임 중복검사
//        String nickname = requestDto.getNickName();
//        if (userRepository.existsByNickname(nickname)) {
//            throw new CustomException(ErrorCode.SIGNUP_NICKNAME_DUPLICATE_CHECK);
//        } else {
//            user.setNickname(requestDto.getNickName());
//            user.setStacks(requestDto.getStacks());
//            user.setProfileImg(uploadImageUrl);
//            return uploadImageUrl;
//
//        }
//    }
//
//    //S3로 업로드
//    private String putS3(File uploadFile, String fileName) {
//        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
//                .withCannedAcl(CannedAccessControlList.PublicRead));
//        return amazonS3Client.getUrl(bucket, fileName).toString();
//    }
//
//    //로컬에 저장된 이미지 지우기
//    private void removeNewFile(File targetFile) {
//        if (targetFile.delete()) {
//            log.info("파일이 삭제되었습니다.");
//        } else {
//            log.info("파일이 삭제되지 못했습니다.");
//        }
//    }
//
//    private Optional<File> convert(MultipartFile file) throws IOException {
//        File convertFile = new File(file.getOriginalFilename());
//        if(convertFile.createNewFile()) {
//            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
//                fos.write(file.getBytes());
//            }
//            return Optional.of(convertFile);
//        }
//
//        return Optional.empty();
//    }
//}