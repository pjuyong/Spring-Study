package com.likelion.sbstudy.domain.auth.mapper;

import com.likelion.sbstudy.domain.auth.dto.response.LoginResponse;
import com.likelion.sbstudy.domain.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

  public LoginResponse toLoginResponse(User user, String accessToken, Long expirationTime) {
    return LoginResponse.builder()
        .accessToken(accessToken)
        .userId(user.getId())
        .username(user.getUsername())
        .role(user.getRole())
        .expirationTime(expirationTime)
        .build();
  }
}