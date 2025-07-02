package com.likelion.sbstudy.domain.auth.controller;

import com.likelion.sbstudy.domain.auth.dto.request.LoginRequest;
import com.likelion.sbstudy.domain.auth.dto.response.LoginResponse;
import com.likelion.sbstudy.domain.auth.service.AuthService;
import com.likelion.sbstudy.domain.user.exception.UserErrorCode;
import com.likelion.sbstudy.domain.user.repository.UserRepository;
import com.likelion.sbstudy.global.exception.CustomException;
import com.likelion.sbstudy.global.jwt.JwtProvider;
import com.likelion.sbstudy.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auths")
@Tag(name = "Auth", description = "Auth 관리 API")
public class AuthController {

  private final AuthService authService;
  private final UserRepository userRepository;
  private final JwtProvider jwtProvider;

  @Operation(summary = "사용자 로그인", description = "사용자 로그인을 위한 API")
  @PostMapping("/login")
  public ResponseEntity<BaseResponse<LoginResponse>> login(
      @RequestBody @Valid LoginRequest loginRequest, HttpServletResponse response) {
    LoginResponse loginResponse = authService.login(loginRequest);

    // refreshToken 가져오기
    String refreshToken = userRepository.findByUsername(loginRequest.getUsername())
        .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND))
        .getRefreshToken();

    // Set-Cookie 설정 (HttpOnly + Secure)
    jwtProvider.addJwtToCookie(response, refreshToken, "refreshToken", 60 * 60 * 24 * 7);

    return ResponseEntity.ok(BaseResponse.success("로그인에 성공했습니다.", loginResponse));
  }
}