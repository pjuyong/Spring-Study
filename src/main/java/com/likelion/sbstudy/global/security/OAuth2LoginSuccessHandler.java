package com.likelion.sbstudy.global.security;

import com.likelion.sbstudy.domain.user.entity.User;
import com.likelion.sbstudy.domain.user.exception.UserErrorCode;
import com.likelion.sbstudy.domain.user.repository.UserRepository;
import com.likelion.sbstudy.global.exception.CustomException;
import com.likelion.sbstudy.global.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

  private final JwtProvider jwtProvider;
  private final UserRepository userRepository;

  @Override
  @Transactional
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException {
    OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
    String provider = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
    String email = null;

    switch (provider) {
      case "kakao":
        Map<String, Object> kakaoAccount = oAuth2User.getAttribute("kakao_account");
        if (kakaoAccount != null) {
          email = (String) kakaoAccount.get("email");
        }
        break;
      case "naver":
        Map<String, Object> naverResponse = oAuth2User.getAttribute("response");
        if (naverResponse != null) {
          email = (String) naverResponse.get("email");
        }
        break;
      case "google":
        email = oAuth2User.getAttribute("email");
        break;
      default:
        throw new CustomException(UserErrorCode.USER_NOT_FOUND);
    }

    if (email == null) {
      throw new CustomException(UserErrorCode.USER_NOT_FOUND);
    }

    User user = userRepository.findByUsername(email)
        .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

    String accessToken = jwtProvider.createAccessToken(
        user.getUsername(),
        user.getRole().toString(),
        provider
    );

    String refreshToken = jwtProvider.createRefreshToken(
        user.getUsername(),
        UUID.randomUUID().toString()
    );

    user.createRefreshToken(refreshToken);

    log.info("로그인 성공: {}", user.getUsername());

    response.addHeader("Authorization", "Bearer " + accessToken);
    response.sendRedirect("/swagger-ui/index.html#/");
  }
}