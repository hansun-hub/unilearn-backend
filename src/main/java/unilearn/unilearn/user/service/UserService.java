package unilearn.unilearn.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import unilearn.unilearn.user.config.UserSecurityAdapter;
import unilearn.unilearn.user.domain.LoginForm;
import unilearn.unilearn.user.domain.School;
import unilearn.unilearn.user.domain.SignUpForm;
import unilearn.unilearn.user.domain.User;
import unilearn.unilearn.user.exception.UserNotValidException;
import unilearn.unilearn.user.jwt.JwtTokenProvider;
import unilearn.unilearn.user.repository.SchoolRepository;
import unilearn.unilearn.user.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final SchoolRepository schoolRepository;
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
                .build();
        return userRepository.save(user);
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
}
