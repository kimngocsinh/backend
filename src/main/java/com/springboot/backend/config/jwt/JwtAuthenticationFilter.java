package com.springboot.backend.config.jwt;

import com.springboot.backend.entity.User;
import com.springboot.backend.service.jwt.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Lấy header authorization
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Lấy token JWT (bỏ "Bearer ")
        jwt = authHeader.substring(7);
        username = jwtService.extractUsername(jwt); // hoặc jwtService.extractAllClaims(jwt).getSubject();

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Tải thông tin user từ DB
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtService.isTokenValid(jwt, (User) userDetails)) {

                Claims claims = jwtService.extractAllClaims(jwt);

                // Lấy danh sách role từ claims
                String roleCode = claims.get("scope", String.class);
                List<String> roleList = List.of(roleCode);

                //Custom lại role bắt đầu bằng "ROLE_"
                //Neu dùng thư viện Oauth2 thì dùng hàm jwtAuthenticationConverter để customize lại
                Set<GrantedAuthority> authorities = roleList.stream()
                        .map(SimpleGrantedAuthority :: new) // hoac ROLE_ nếu dùng hasRole
                        .collect(Collectors.toSet());

                // Tạo authentication token cho Spring Security
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, //principal
                        null, // không cần vì đã xác thực
                        authorities); // quyền
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // thông tin chi tiết về request: IP/session/log

                authentication.getAuthorities().forEach(grantedAuthority -> logger.info(grantedAuthority.getAuthority()));

                // Đặt authentication cho context để các filter sau nhận diện
                // Để hệ thống biết rằng request đã được xác thưực
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}
