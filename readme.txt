Spring security jwt
- bảo mật ứng dụng dùng JSON Web Token (JWT) để xác thực (authentication) và ủy quyền (authorization)

Cách thức hoạt động:
client (người dùng) gửi request (đăng nhập) (id/password) vào 1 ứng dụng -> spring sẽ xác thực thông tin đăng nhập
-> xác thực thành công: tạo jwt token và trả về cho client và được lưu (LocalStorage, SessionStorage, hoặc Cookie)
-> mỗi request sau, client gửi token trong phần authorization header có dạng: Authorization: Bearer <jwt_token>
để chứng minh rằng request đã được xác thực

Thành phần của JWT:
Header.Payload.Signature
- Header: 2 thành phần: loại token và thuật toán ký
- Payload: Chứa thông tin(claims) về đối tượng truy cập hệ thống (user) và các thông tin bổ sung
- Signature: phần ký để xác minh token chưa bị sửa đổi

Cách thức triển khai:
Bổ sung thư viện jjwt (có thể dùng thư viện khác) vào file pom.xml:
<dependency>
   <groupId>io.jsonwebtoken</groupId>
   <artifactId>jjwt-api</artifactId>
   <version>0.11.5</version>
  </dependency>

  <dependency>
   <groupId>io.jsonwebtoken</groupId>
   <artifactId>jjwt-impl</artifactId>
   <version>0.11.5</version>
  </dependency>

  <dependency>
   <groupId>io.jsonwebtoken</groupId>
   <artifactId>jjwt-jackson</artifactId>
   <version>0.11.5</version>
  </dependency>

- tạo class(thường là entiry user hoặc có thể tạo class khác) implement UserDetails :là một interface được thiết kế riêng để sử dụng cho JWT
trong việc biểu diễn những thông tin chi tiết của user

- Tạo class implement UserDetailsService có trách nhiệm đọc thông tin user từ DB
và phải Override lại phương thức loadUserByUsername để get user từ DB theo tên đăng nhập(username)

- Tạo class thực hiện các phương thức (@Service hoặc @Component):
+ Tạo token: được sử dụng khi người dùng login nếu thông tin login chính xác thì sẽ trả về jwt token
+ Trích xuất username từ token: trích xuất thông tin user từ request để biết vai trò, quyền hạn của user
+ Kiểm tra token có hết hạn hay không
+ Token có hợp lệ hay không

- Tạo class implement AuthenticationEntryPoint (@Component)
override method commence(): để handle các exception liên quan tới xác thực (authentication)
thường là 401. Nếu muốn custom nhiều hơn tạo class xử lý handle exception

- Tạo class implement OncePerRequestFilter (@Component):
override lại method: void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {}

=> Có bất cứ request nó sẽ đi qua method "doFilterInternal" trước tiên và thực hiện 1 số thao tác:
+ Lấy token từ request và trích xuất thông tin user
+ Đọc thông tin user (load userdetails)
+ Kiểm tra tính hợp lệ của token: kiểm tra thông tin user có khớp với data trong DB và token vẫn còn hạn
+ Setup context based cho việc xác thực thông qua các thông tin đã đọc được:
    SecurityContextHolder.getContext().setAuthentication(authentication);


- Tạo class đóng vai trò cấu hình spring security cho toàn bộ ứng dụng (ở đây là SecurityConfig)
+ @Configuration: Đánh dấu đây là một class sử dụng cho việc configuration
+ @EnableMethodSecurity: Kích hoạt bảo mật cấp phương thức của Spring Security

+ Tạo các @Bean để sử dụng tòoàn bộ hệ thống:
  * Tạo method: SecurityFilterChain securityFilterChain(HttpSecurity http): cấu hình điều kiện filter
  * AuthenticationManager authenticationManager(AuthenticationConfiguration config): chịu trách nhiệm xác thực người dùng
    tự động kiểm tra username và password
  * PasswordEncoder passwordEncoder(): Tự động mã hóa và kiểm tra mật khẩu
        Nó sẽ được gọi ở : Authentication auth = authenticationManager.authenticate(
                               new UsernamePasswordAuthenticationToken(username, password)
                           );
        trong controller.

- Tạo controller:
+ Authentication auth = authenticationManager.authenticate(
                                 new UsernamePasswordAuthenticationToken(username, password)
                             );
  -> tự động validate username và password xem có hợp lệ không
  Hợp lệ gán vào : SecurityContextHolder.getContext().setAuthentication(authentication);
  -> Kiểm tra hợp lệ xong lấy ra thông tin user: User userDetails = (User) authentication.getPrincipal();
  và tạo token từ thông tin user trên.


* Ngoài phân quyền theo endpoint thì còn phân quyền theo method
