package com.likelion.sbstudy.global.jwt;

import com.likelion.sbstudy.domain.auth.exception.AuthErrorCode;
import com.likelion.sbstudy.global.exception.CustomException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtProvider {

  private final Key key;
  private final long accessTokenExpireTime;
  private final long refreshTokenExpireTime;

  public JwtProvider(
      @Value("${spring.jwt.secret}") String secretKey,
      @Value("${spring.jwt.access-token-expire-time}") long accessTokenExpireTime,
      @Value("${spring.jwt.refresh-token-expire-time}") long refreshTokenExpireTime) {
    byte[] keyBytes = java.util.Base64.getDecoder().decode(secretKey);
    this.key = Keys.hmacShaKeyFor(keyBytes);
    this.accessTokenExpireTime = accessTokenExpireTime;
    this.refreshTokenExpireTime = refreshTokenExpireTime;
  }

  public String createAccessToken(String username, String role, String provider) {
    Date now = new Date();
    Claims claims = Jwts.claims()
        .setSubject(username)
        .setId(username); // tokenId 대용
    claims.put("roles", role);         // ex: ["ROLE_USER"]
    claims.put("provider", provider);   // ex: "google"
    return Jwts.builder()
        .setSubject(username)
        .setId(String.valueOf(username))
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + accessTokenExpireTime))
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  public long getExpiration(String accessToken) {
    Claims claims = parseClaims(accessToken);

    Date expiration = claims.getExpiration();
    long now = System.currentTimeMillis();
    return expiration.getTime() - now;
  }

  private Key getSigningKey() {
    return key;
  }

  public String createRefreshToken(String username, String tokenId) {
    Date now = new Date();
    return Jwts.builder()
        .setSubject(username)
        .setId(tokenId)
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + refreshTokenExpireTime))
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  public void addJwtToCookie(HttpServletResponse response, String token, String name, long maxAge) {
    Cookie cookie = new Cookie(name, token);
    cookie.setHttpOnly(true);
    //cookie.setSecure(true); // HTTPS 환경에서만 전송되게
    cookie.setPath("/");
    cookie.setMaxAge((int) maxAge / 1000); // 단위: 초
    response.addCookie(cookie);
  }

  public boolean validateToken(String token) { // 토큰이 유효한지(만료되었는지, 지원하는 형식인지 등) 검사
    try {
      parseClaims(token);
      return true;
    } catch (ExpiredJwtException e) {
      throw new CustomException(AuthErrorCode.JWT_TOKEN_EXPIRED);
    } catch (UnsupportedJwtException e) {
      throw new CustomException(AuthErrorCode.UNSUPPORTED_TOKEN);
    } catch (MalformedJwtException e) {
      throw new CustomException(AuthErrorCode.MALFORMED_JWT_TOKEN);
    } catch (io.jsonwebtoken.SignatureException e) {
      throw new CustomException(AuthErrorCode.INVALID_SIGNATURE);
    } catch (IllegalArgumentException e) {
      throw new CustomException(AuthErrorCode.ILLEGAL_ARGUMENT);
    }
  }

  public String extractSocialId(String token) {
    return parseClaims(token).getSubject();
  }

  public String extractTokenId(String token) {
    return parseClaims(token).getId();
  }

  private Claims parseClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }
}