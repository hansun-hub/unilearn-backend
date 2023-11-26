package unilearn.unilearn.user.service;

//import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
//import unilearn.unilearn.global.config.S3Config;
import org.springframework.web.multipart.MultipartFile;
import unilearn.unilearn.user.dto.LoginForm;
import unilearn.unilearn.user.dto.UserDto;
import unilearn.unilearn.user.entity.School;
import unilearn.unilearn.user.dto.SignUpForm;
import unilearn.unilearn.user.entity.Temperature;
import unilearn.unilearn.user.entity.User;
import unilearn.unilearn.user.jwt.JwtTokenProvider;
import unilearn.unilearn.user.repository.SchoolRepository;
import unilearn.unilearn.user.repository.TemperatureRepository;
import unilearn.unilearn.user.repository.UserRepository;

import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final SchoolRepository schoolRepository;
    private final TemperatureRepository temperatureRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final MailService mailService;

    private String createCode() {
        int lenth = 4;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < lenth; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            log.debug("MemberService.createCode() exception occur");
            throw new IllegalArgumentException("CAN NOT CREATE CODE");
        }
    }

    public User processNewUser(SignUpForm signUpForm){
        String title = "Unilearn 이메일 인증 번호";
        String authCode = createCode();
        mailService.sendEmail(signUpForm.getEmail(), title, authCode);

        return saveNewUser(signUpForm, authCode);
    }

    private User saveNewUser(SignUpForm signUpForm, String authCode){
        School school = schoolRepository.findBySchoolName("서울여자대학교");

        User user = User.builder()
                .loginId(signUpForm.getLoginId())
                .email(signUpForm.getEmail())
                .username(signUpForm.getUsername())
                .nickname(signUpForm.getNickname())
                .school(school)
                .emailChecked(false)
                .emailCouont(3)
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .roles(Collections.singletonList("ROLE_USER"))
                .major("학과 미인증")
                .studentId("학번 미인증")
                .build();
        User newUser = userRepository.save(user);

        newUser.createCheckEmailToken(authCode);

        Temperature temperature = Temperature.builder()
                .user(user)
                .temperature(36.5)
                .build();
        temperatureRepository.save(temperature);

        return newUser;
    }

    private void getAuthentication(String loginId, String password){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginId, password);
        authenticationManager.authenticate(authenticationToken);
    }

    private String getJwtToken(User user){
        return jwtTokenProvider.createToken(user.getLoginId(), user.getAuthorities()
                .stream().map(Object::toString).collect(Collectors.toList()));
    }

    public List<String> getSignUpErrorList(BindingResult bindingResult){
        return bindingResult.getGlobalErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
    }

    public ResponseEntity<Map<String, String>> getSuccessSignUpResponse(SignUpForm signUpForm){
        if (!checkValidUser(signUpForm)) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        User user = processNewUser(signUpForm);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("UserId", user.getId().toString());
        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }

    private boolean checkValidUser(SignUpForm signUpForm) {
        User user = userRepository.findByEmail(signUpForm.getEmail());
        if (user != null)
            return false;
        user = userRepository.findByNickname(signUpForm.getNickname());
        if (user != null)
            return false;
        user = userRepository.findByLoginId(signUpForm.getLoginId());
        if (user != null)
            return false;
        return true;
    }

    public ResponseEntity login(String loginId, String password){
        User user = userRepository.findByLoginId(loginId);
        if (user == null){
            throw new UsernameNotFoundException(String.format("ID: [%s]를 찾을 수 없습니다.", loginId));
        }
        if (!passwordEncoder.matches(password, user.getPassword())){
            throw new IllegalArgumentException("패스워드가 일치하지 않습니다.");
        }
        if (!user.isEmailChecked()) {
            throw new IllegalArgumentException("인증되지 않은 사용자입니다.");
        }
        getAuthentication(loginId, password);
        Map<String,String> responseBody = new HashMap<>();
        responseBody.put("token", getJwtToken(user));
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public UserDto.UserResponseDto getUserProfile(Principal principal) {
        User user = userRepository.findByNickname(principal.getName());
        return UserDto.UserResponseDto.builder()
                .user_id(user.getId())
                .nickname(user.getNickname())
                .major(user.getMajor())
                .student_id(user.getStudentId())
                .introduction(user.getIntroduction())
                .school(user.getSchool().getSchoolName())
                .temperature(user.getTemperature().getTemperature())
                .build();
    }

    public void updateUserProfile(UserDto.UserRequestDto form, Principal principal) {
        User user = userRepository.findByNickname(principal.getName());
        user.updateIntroduction(form.getIntroduction());
        userRepository.save(user);
    }

    public ResponseEntity<Map<String, String>> verifiedCode(String email, String authCode) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Map<String, String> responseBody = new HashMap<>();
        // 이미 인증된 사용자라면 x
        if (user.isEmailChecked()) {
            responseBody.put("UserId", user.getId().toString());
            return ResponseEntity.status(HttpStatus.OK).body(responseBody);
        }
        log.info("check token = " + user.getCheckEmailToken() + " / auth code = " + authCode);

        if (user.getCheckEmailToken().equals(authCode)) {
            user.setEmailChecked(true);
            userRepository.save(user);
            responseBody.put("UserId", user.getId().toString());
            return ResponseEntity.status(HttpStatus.OK).body(responseBody);
        } else {
            user.setEmailCouont(user.getEmailCouont() - 1);
            userRepository.save(user);
            responseBody.put("emailCount", user.getEmailCouont().toString());
            if (user.getEmailCouont() == 0) {
                userRepository.delete(user);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        }
    }
}
