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

import java.security.Principal;
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

    public User processNewUser(SignUpForm signUpForm){
        // TODO send email and confirm
        return saveNewUser(signUpForm);
    }

    private User saveNewUser(SignUpForm signUpForm){
        School school = schoolRepository.findBySchoolName("서울여자대학교");

        User user = User.builder()
                .loginId(signUpForm.getLoginId())
                .email(signUpForm.getEmail())
                .username(signUpForm.getUsername())
                .nickname(signUpForm.getNickname())
                .school(school)
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .roles(Collections.singletonList("ROLE_USER"))
                .major("학과 미인증")
                .studentId("학번 미인증")
                .build();
        User newUser = userRepository.save(user);

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
        User user = processNewUser(signUpForm);

        LoginForm form = new LoginForm();
        form.setLoginId(signUpForm.getLoginId());
        form.setPassword(signUpForm.getPassword());
        String jwtToken = login(form);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("UserId", user.getId().toString());
        responseBody.put("Token", jwtToken);
        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }

    public String login(LoginForm form){
        User user = userRepository.findByLoginId(form.getLoginId());
        if (user == null){
            throw new UsernameNotFoundException(String.format("ID: [%s]를 찾을 수 없습니다.", form.getLoginId()));
        }
        if (!passwordEncoder.matches(form.getPassword(), user.getPassword())){
            throw new IllegalArgumentException("패스워드가 일치하지 않습니다.");
        }
        getAuthentication(form.getLoginId(), form.getPassword());
        return getJwtToken(user);
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

    // 미완성
    public void updateUserAuth(UserDto.UserAuthRequestDto form, Principal principal, MultipartFile images) {

    }
}
