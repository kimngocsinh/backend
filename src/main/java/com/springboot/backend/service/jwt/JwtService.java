package com.springboot.backend.service.jwt;

import com.springboot.backend.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.security.Key;
import java.util.Date;
import java.util.StringJoiner;

@Service
public class JwtService {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expirationMs}")
    private long jwtExpirationInMs; // thời gian hết hạn token(ms)

    /**
     * Lấy secret key và mã hóa xuống byte[]
     * @return : Key: secret key đã được mã hóa
     */
    public Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Tọa JWT cho người dùng
     * @param user : người dùng
     * @return : chuỗi JWT
     */
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date()) // Thời điểm khởi tạo
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .claim("scope", buildScope(user)) // quyền USER/ADMIN,....
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Lấy username từ token
     * @param token
     * @return username trong token
     */
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Kiểm tra token đã hết hạn chưa
     * @param token JWT
     * @return true nếu đã hết hạn
     */
    public boolean isTokenExpired(String token) {
        final Date expiDate = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token).getBody().getExpiration(); // Lấy ngày hết hạn
        return expiDate.before(new Date()); // so sánh ngày hết hạn với thời điểm hiện tại
    }

    /**
     * Kiểm tra token còn hợp lệ với người dùng k
     * @param token
     * @param user
     * @return
     */
    public boolean isTokenValid(String token, User user) {
        final String username = extractUsername(token);
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Get ra các role của user
     * @param user
     * @return: role của user
     */
    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> stringJoiner.add(role.getCode()));
        }
        return stringJoiner.toString();
    }

    /**
     * Giải mã payload của token: thông tin đăng nhập
     * @param token
     * @return Claims (thông tin user đăng nhập)
     */
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token).getBody();
    }
}
