package com.likelion.sbstudy.global.s3.controller;

import com.likelion.sbstudy.global.response.BaseResponse;
import com.likelion.sbstudy.global.s3.dto.S3Response;
import com.likelion.sbstudy.global.s3.entity.PathName;
import com.likelion.sbstudy.global.s3.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/s3")
@Tag(name = "S3", description = "이미지 관리 API")
public class S3Controller {

  private final S3Service s3Service;

  @Operation(summary = "이미지 업로드 API", description = "이미지를 업로드하고 URL을 리턴받는 API")
  @PostMapping(value = "/image-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<BaseResponse<S3Response>> uploadImage(
      @RequestParam PathName pathName, MultipartFile file) {

    S3Response s3Response = s3Service.uploadImage(pathName, file);
    return ResponseEntity.ok(BaseResponse.success("이미지 업로드에 성공했습니다.", s3Response));
  }

  @Operation(summary = "S3 파일 전체 조회 API", description = "해당 경로의 모든 파일 목록을 조회합니다.")
  @GetMapping("/image-list")
  public ResponseEntity<BaseResponse<List<String>>> listFiles(@RequestParam PathName pathName) {
    List<String> files = s3Service.getAllFiles(pathName);
    return ResponseEntity.ok(BaseResponse.success("파일 목록 조회에 성공했습니다.", files));
  }

  @Operation(summary = "S3 파일 삭제 API", description = "파일명을 기반으로 이미지를 삭제합니다.")
  @DeleteMapping("/{pathName}/{fileName}")
  public ResponseEntity<BaseResponse<String>> deleteFile(
      @PathVariable PathName pathName,
      @PathVariable String fileName) {
    s3Service.deleteFile(pathName, fileName);
    return ResponseEntity.ok(BaseResponse.success("파일 삭제에 성공했습니다."));
  }
}