package com.likelion.sbstudy.domain.user.service;

import com.likelion.sbstudy.domain.user.dto.request.SignUpRequest;
import com.likelion.sbstudy.domain.user.dto.response.SignUpResponse;
import com.likelion.sbstudy.domain.user.entity.User;
import com.likelion.sbstudy.domain.user.exception.UserErrorCode;
import com.likelion.sbstudy.domain.user.mapper.UserMapper;
import com.likelion.sbstudy.domain.user.repository.UserRepository;
import com.likelion.sbstudy.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper;

  @Transactional
  public SignUpResponse signUp(SignUpRequest request) {
    if (userRepository.existsByUsername(request.getUsername())) { // 아이디가 DB에 있는 것과 같으면 안됨
      throw new CustomException(UserErrorCode.USERNAME_ALREADY_EXISTS); // 아이디가 같으면 예외처리
    }

    // entity에서 @notBlank를 설정했기 때문에 필요 없음
    /*if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
      throw new CustomException(UserErrorCode.PASSWORD_REQUIRED);
    }
*/
    // 비밀번호 인코딩 (암호화)
    String encodedPassword = passwordEncoder.encode(request.getPassword());

    // 유저 엔티티 생성
    User user = User.builder()
        .username(request.getUsername())
        .password(encodedPassword)
        .provider("custom") // 사비스 자체 회원가입 임을 명시
        .build();

    // 저장 및 로깅
    User savedUser = userRepository.save(user);
    log.info("새로운 사용자 생성: {}", savedUser.getUsername());

    return userMapper.toSignUpResponse(savedUser);
  }
}
