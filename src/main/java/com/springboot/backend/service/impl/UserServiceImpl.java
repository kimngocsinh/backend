package com.springboot.backend.service.impl;

import com.springboot.backend.config.Constants;
import com.springboot.backend.dto.response.ApiResponse;
import com.springboot.backend.dto.UserDto;
import com.springboot.backend.entity.Role;
import com.springboot.backend.entity.User;
import com.springboot.backend.payload.RegisterResponse;
import com.springboot.backend.repository.RoleRepository;
import com.springboot.backend.repository.UserRepository;
import com.springboot.backend.service.UserService;
import com.springboot.backend.service.jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService  jwtService;

//    private final UserMapper userMapper;

    @PreAuthorize("hasRole('ADMIN')") // kiểm tra role truớc khi vào method
    @Override
    public ApiResponse<List<UserDto>> getAllUser(HttpServletRequest request) {
        List<UserDto> users = userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ApiResponse.success(users, request.getRequestURI());
    }

    @Override
    public ApiResponse<UserDto> getMyInfo(HttpServletRequest request) {
        // Chứa thông tin user đang đăng nhập
        SecurityContext securityContext = SecurityContextHolder.getContext();
        String name = securityContext.getAuthentication().getName();
        Optional<User> userOpt = userRepository.findByUsername(name);
        if (userOpt.isEmpty()) {
            return ApiResponse.error(null, Constants.USER_NOT_FOUND, request.getRequestURI());
        }

        User user = userOpt.get();

        // Ánh xạ User -> UserDto
        UserDto dto = convertToDto(user);

        return ApiResponse.success(dto, request.getRequestURI());
    }

    /**
     * Get user theo id
     * @param id : mã id cần lấy ra
     * @return: thông tin user
     */
//    @PostAuthorize("returnObject.data.username == authentication.name") // truy cập vào phương thức sau đó so sánh với role, chỉ cho phép user đăng nhập
    @Override                                                      // xem thông tin của mình
    public ApiResponse<UserDto> getUser(Long id, HttpServletRequest request) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            return ApiResponse.error(null, Constants.USER_NOT_FOUND, request.getRequestURI());
        }

        User user = userOpt.get();

        // Ánh xạ User -> UserDto
        UserDto dto = convertToDto(user);

        return ApiResponse.success(dto, request.getRequestURI());
    }

    /**
     * Đăng ký user
     * @param userDto : thông tin user đăng ký
     * @return: Thông tin user và token
     */
    @Override
    public ApiResponse<RegisterResponse> createUser(UserDto userDto, HttpServletRequest request) {

        if (userRepository.existsByUsername(userDto.getUsername())) {
            return ApiResponse.error(null, Constants.USR_EXISTS, request.getRequestURI());
        }
        // Tạo đối tượng User từ DTO
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setAddress(userDto.getAddress());
        user.setStatus(userDto.getStatus());

        // Gán password mặc định hoặc để xử lý riêng
        String defaultPassword = "admin123";
        String password = userDto.getPassword() != null ? userDto.getPassword() : defaultPassword;
        user.setPassword(passwordEncoder.encode(password));

        // Lấy danh sách Role từ code
        Set<Role> roles = new HashSet<>();
        if (userDto.getRoles() != null) {
            for (String roleCode : userDto.getRoles()) {
                Role role = roleRepository.findByCode(roleCode)
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy role: " + roleCode));
                roles.add(role);
            }
        }
        user.setRoles(roles);

        User savedUser = userRepository.save(user);

        String token = jwtService.generateToken(savedUser);

        // Chuyển lại thành UserDto để trả về
        RegisterResponse registerResponse = new RegisterResponse();
        UserDto dto = convertToDto(user);

        registerResponse.setUser(dto);
        registerResponse.setToken(token);

        return ApiResponse.success(registerResponse, request.getRequestURI());
    }

    /**
     * Upadte user
     * @param userDto
     * @param request
     * @return ApiResponse<UserDto>
     */
    @Override
    public ApiResponse<UserDto> updateUser(Long id, UserDto userDto, HttpServletRequest request) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ApiResponse.error(null, Constants.USER_NOT_FOUND, request.getRequestURI());
        }
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setEmail(userDto.getEmail());
        user.setAddress(userDto.getAddress());
        user.setStatus(userDto.getStatus());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);
        UserDto dto = convertToDto(user);
        return ApiResponse.success(dto, request.getRequestURI());
    }

    @Override
    public ApiResponse<UserDto> deleteUser(Long id, HttpServletRequest request) {
        if (!userRepository.existsById(id)) {
            return ApiResponse.error(null, Constants.USER_NOT_FOUND, request.getRequestURI());
        }
        userRepository.deleteById(id);
        return ApiResponse.success(null, request.getRequestURI());
    }

    /**
     * Convert tu Entity sang Dto
     * @param user
     * @return UserDto
     */
    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setAddress(user.getAddress());
        dto.setStatus(user.getStatus());
        dto.setCreatedBy(user.getCreatedBy());
        dto.setCreatedDate(user.getCreatedDate());
        dto.setModifiedBy(user.getModifiedBy());
        dto.setModifiedDate(user.getModifiedDate());
        Set<String> setRoles = user.getRoles().stream()
                .map(Role::getCode)
                .collect(Collectors.toSet());
        dto.setRoles(setRoles);
        return dto;
    }
}
