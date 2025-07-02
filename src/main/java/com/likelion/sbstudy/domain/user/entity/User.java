package com.likelion.sbstudy.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.likelion.sbstudy.global.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "username", nullable = false, unique = true) // 사용자의 아이디(반드시 고유해야 함)
  private String username;

  @JsonIgnore // 응답 시 데이터를 json 형식으로 보낼때 이 부분은 보내지 않는다
  @Column(name = "password")
  private String password;

  @Column(name = "provider", nullable = false)
  private String provider;

  @JsonIgnore
  @Column(name = "refresh_token")
  private String refreshToken;

  @Column(name = "role", nullable = false)
  @Enumerated(EnumType.STRING)
  @Builder.Default // 빌더 사용 시에 이 필드를 명시해주지 않아도 디폴트로 생성
  private Role role = Role.USER;

  public void createRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public static User fromOAuth(String email, String provider) {
    return User.builder()
        .username(email)
        .provider(provider)
        .role(Role.USER)
        .build();
  }
}