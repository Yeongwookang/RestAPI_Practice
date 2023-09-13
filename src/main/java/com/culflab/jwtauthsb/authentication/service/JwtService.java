package com.culflab.jwtauthsb.authentication.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY="5710bf98ba6ed373f2a03ec947ac6bc31755d3a10284dbcc45a6dad9216e6f6c65a1aa56693621d2fc6cf927573ad4ac768b55075c63a1d5aebfd9888f8d23dd6cf7a964f65711bc0b0db512c5e6399fdab80f0b974dab5bda1b2033a79e7bd11d3ad77605f727252a1ddcea8bb64c7c68b7fc6f1f48142a25386b813d24dbedc66c7b0247a8610c22e09333529a76abb4185b6a52d565a3bfabd32697ef1fed014ed5140286cab0105788742e63aec14c21be3706f708458dbe60c1b0a65ad1d46ce23d7ed1cad8dd86904af84129d5e3b86722244bec63393a9c150b5275f412db55db0ae68eae235cddc4ecf7225494e06a00592b41c5784e6f2c3be2ef31";
    private static final int EXPIRE_TIME = 1000*60*30; // 만료시간 [ms] 단위, 30분이다.
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    //Claims를 토큰으로 매핑
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }
    // new HashMap<>() 자리에 추가적인 값을 넣을수 있다

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenVaild(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpriation(token).before(new Date());
    }

    private Date extractExpriation(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
