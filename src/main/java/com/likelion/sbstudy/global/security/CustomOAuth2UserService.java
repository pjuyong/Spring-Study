package com.likelion.sbstudy.global.security;

import com.likelion.sbstudy.domain.user.entity.User;
import com.likelion.sbstudy.domain.user.repository.UserRepository;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

  private final UserRepository userRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
    OAuth2User oauth2User = new DefaultOAuth2UserService().loadUser(request);
    Map<String, Object> attributes = oauth2User.getAttributes();
    String provider = request.getClientRegistration().getRegistrationId();
    String email;

    switch (provider) {
      case "google" -> email = (String) attributes.get("email");
      case "kakao" -> {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        email = (String) kakaoAccount.get("email");
      }
      case "naver" -> {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        email = (String) response.get("email");
      }
      default -> throw new OAuth2AuthenticationException("Unknown provider: " + provider);
    }

    User user = userRepository.findByUsername(email)
        .orElseGet(() -> userRepository.save(
            User.fromOAuth(email, provider)));

    String nameAttributeKey = switch (provider) {
      case "google" -> "email";
      case "kakao" -> "id";      // 카카오는 고유 ID로 식별
      case "naver" -> "resultcode";      // 네이버도 response.id가 있음
      default -> throw new OAuth2AuthenticationException("Unknown provider: " + provider);
    };

    return new DefaultOAuth2User(
        Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
        attributes,
        nameAttributeKey
    );
  }
}
