package com.springboot.backend.service.impl;

import com.springboot.backend.config.Constants;
import com.springboot.backend.dto.UserDto;
import com.springboot.backend.entity.ResponseDto;
import com.springboot.backend.entity.Role;
import com.springboot.backend.entity.User;
import com.springboot.backend.payload.RegisterResponse;
import com.springboot.backend.repository.RoleRepository;
import com.springboot.backend.repository.UserRepository;
import com.springboot.backend.service.UserService;
import com.springboot.backend.service.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
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
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService  jwtService;

    @PreAuthorize("hasRole('ADMIN')") // kiểm tra role truớc khi vào method
    @Override
    public List<ResponseDto<UserDto>> getAllUser() {
        return userRepository.findAll().stream()
                .map(user -> {
                    UserDto dto = new UserDto();
                    dto.setId(user.getId());
                    dto.setUsername(user.getUsername());
                    dto.setEmail(user.getEmail());
                    dto.setPhone(user.getPhone());
                    dto.setAddress(user.getAddress());
                    dto.setStatus(user.getStatus());
                    dto.setRoleId(user.getRoleId());

                    // Convert Set<Role> -> Set<String> (role code)
                    Set<String> roleCodes = user.getRoles()
                            .stream()
                            .map(Role::getCode)
                            .collect(Collectors.toSet());
                    dto.setRoles(roleCodes);
                    return new ResponseDto<>(true, Constants.SUCCESS, dto);
                }).collect(Collectors.toList());

    }

    @Override
    public ResponseDto<UserDto> getMyInfo() {
        // Chứa thông tin user đang đăng nhập
        SecurityContext securityContext = SecurityContextHolder.getContext();
        String name = securityContext.getAuthentication().getName();
        Optional<User> userOpt = userRepository.findByUsername(name);
        if (userOpt.isEmpty()) {
            return new ResponseDto<>(true, Constants.ERROR, null);
        }

        User user = userOpt.get();

        // Ánh xạ User -> UserDto
        UserDto dto = new UserDto();
        dto.setId(user.getId()); // kế thừa từ BaseDto/BaseEntity
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setAddress(user.getAddress());
        dto.setStatus(user.getStatus());
        dto.setRoleId(user.getRoleId());

        // Convert Set<Role> -> Set<String> (role code)
        Set<String> roleCodes = user.getRoles()
                .stream()
                .map(Role::getCode)
                .collect(Collectors.toSet());
        dto.setRoles(roleCodes);

        return new ResponseDto<>(true, Constants.SUCCESS, dto);
    }

    /**
     * Get user theo id
     * @param id : mã id cần lấy ra
     * @return: thông tin user
     */
    @PostAuthorize("returnObject.data.username == authentication.name") // truy cập vào phương thức sau đó so sánh với role, chỉ cho phép user đăng nhập
    @Override                                                      // xem thông tin của mình
    public ResponseDto<UserDto> getUser(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            return new ResponseDto<>(true, Constants.ERROR, null);
        }

        User user = userOpt.get();

        // Ánh xạ User -> UserDto
        UserDto dto = new UserDto();
        dto.setId(user.getId()); // kế thừa từ BaseDto/BaseEntity
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setAddress(user.getAddress());
        dto.setStatus(user.getStatus());
        dto.setRoleId(user.getRoleId());

        // Convert Set<Role> -> Set<String> (role code)
        Set<String> roleCodes = user.getRoles()
                .stream()
                .map(Role::getCode)
                .collect(Collectors.toSet());
        dto.setRoles(roleCodes);

        return new ResponseDto<>(true, Constants.SUCCESS, dto);
    }

    /**
     * Đăng ký user
     * @param userDto : thông tin user đăng ký
     * @return: Thông tin user và token
     */
    @Override
    public ResponseDto<RegisterResponse> createUser(UserDto userDto) {

        if (userRepository.existsByUsername(userDto.getUsername())) {
            return new ResponseDto<>(false, Constants.USER_ALREADY_EXISTS, null);
        }
        // Tạo đối tượng User từ DTO
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setAddress(userDto.getAddress());
        user.setStatus(userDto.getStatus());
        user.setRoleId(userDto.getRoleId());

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

        // 4. Chuyển lại thành UserDto để trả về
        RegisterResponse registerResponse = new RegisterResponse();
        UserDto responseDto = new UserDto();
        responseDto.setId(savedUser.getId());
        responseDto.setUsername(savedUser.getUsername());
        responseDto.setEmail(savedUser.getEmail());
        responseDto.setPhone(savedUser.getPhone());
        responseDto.setAddress(savedUser.getAddress());
        responseDto.setStatus(savedUser.getStatus());
        responseDto.setRoleId(savedUser.getRoleId());

        Set<String> roleCodes = savedUser.getRoles()
                .stream()
                .map(Role::getCode)
                .collect(Collectors.toSet());
        responseDto.setRoles(roleCodes);

        registerResponse.setUser(responseDto);
        registerResponse.setToken(token);

        return new ResponseDto<>(true, Constants.SUCCESS, registerResponse);
    }

    @Override
    public ResponseDto<UserDto> updateUser(UserDto userDto) {
        return null;
    }

    @Override
    public ResponseDto<UserDto> deleteUser(Long id) {
        return null;
    }
}
