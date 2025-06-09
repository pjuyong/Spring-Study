package com.likelion.sbstudy.domain.user.mapper;

import com.likelion.sbstudy.domain.user.dto.response.SignUpResponse;
import com.likelion.sbstudy.domain.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

  public SignUpResponse toSignUpResponse(User user) {
    return SignUpResponse.builder()
        .userId(user.getId())
        .username(user.getUsername())
        .build();
  }
}
