package com.stella.rememberall.security;

import com.stella.rememberall.security.dto.TokenDto;
import com.stella.rememberall.security.exception.AuthErrorCode;
import com.stella.rememberall.security.exception.AuthException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.Base64UrlCodec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtProvider {

    @Value("jwt.secret")
    private String secretKey;
//    @Value("#{new Long('${jwt.token-validity-in-seconds}')}") // "#{new Double('${item.priceFactor}')}"
    private Long accessTokenValidMillisecond = 60 * 60 * 1000L; // 1 hour : 60 * 60 * 1000L
//    @Value("#{new Long('${jwt.refresh-token-validity-in-seconds}')}")
    private Long refreshTokenValidMillisecond = 14 * 60 * 1000L; // 14 day : 14 * 24 * 60 * 60 * 1000L

    private String ROLES = "roles";
    private final UserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        // 암호화
        secretKey = Base64UrlCodec.BASE64URL.encode(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // Jwt 생성
    public TokenDto createTokenDto(Long userPk, List<String> roles, String refreshTokenValue) {
        String accessToken = createAccessToken(userPk, roles);
        String refreshToken = createRefreshToken(refreshTokenValue);

        return TokenDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private String createAccessToken(Long userPk, List<String> roles){
        Claims claims = Jwts.claims().setSubject(String.valueOf(userPk));
        claims.put(ROLES, roles);
        Date now = new Date();

        String accessToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenValidMillisecond))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        return accessToken;
    }

    private String createRefreshToken(String refreshTokenValue){

        Claims claims = Jwts.claims();
        claims.put("value", refreshTokenValue);
        Date now = new Date();

        String refreshToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setExpiration(new Date(now.getTime() + refreshTokenValidMillisecond))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return refreshToken;
    }

    // Jwt 로 인증정보를 조회
    public Authentication getAuthentication(String token) {

        // Jwt 에서 claims 추출
        Claims claims = parseClaims(token);

        // 권한 정보가 없음
        if (claims.get(ROLES) == null) {
            throw new AuthException(AuthErrorCode.AUTHENTICATION_ENTRYPOINT);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getRefreshTokenValue(String refreshToken){
        Claims claims = parseClaims(refreshToken);
        if (claims.get("value") == null){
            throw new AuthException(AuthErrorCode.AUTHENTICATION_ENTRYPOINT);
        }
        log.info("value : "+(String) claims.get("value"));
        return (String) claims.get("value");
    }

    // Jwt 토큰 복호화해서 가져오기
    private Claims parseClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // HTTP Request 의 Header 에서 Token Parsing -> "X-AUTH-TOKEN: jwt"
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

    // jwt 의 유효성 및 만료일자 확인
    public boolean validationToken(String token, HttpServletRequest request) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("잘못된 Jwt 서명입니다.");
            request.setAttribute("exception", AuthErrorCode.MALFORMED_TOKEN);
        } catch (ExpiredJwtException e) {
            log.error("만료된 토큰입니다.");
            request.setAttribute("exception", AuthErrorCode.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.error("지원하지 않는 토큰입니다.");
            request.setAttribute("exception", AuthErrorCode.UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException e) {
            log.error("잘못된 토큰입니다.");
            request.setAttribute("exception", AuthErrorCode.WRONG_TOKEN);
        }
        return false;
    }
}