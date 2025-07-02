package com.likelion.sbstudy.domain.auth.dto.response;

import com.likelion.sbstudy.domain.user.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(title = "LoginResponse DTO", description = "사용자 로그인에 대한 응답 반환")
public class LoginResponse {

  @Schema(description = "사용자 Access Token")
  private String accessToken;

  @Schema(description = "사용자 식별자", example = "1")
  private Long userId;

  @Schema(description = "사용자 아이디 또는 이메일", example = "parkjuyong")
  private String username;

  @Schema(description = "사용자 권한", example = "USER")
  private Role role;

  @Schema(description = "사용자 Access Token 만료 시간", example = "1800000")
  private Long expirationTime;
}