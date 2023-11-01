package unilearn.unilearn.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import unilearn.unilearn.user.config.UserSecurityAdapter;
import unilearn.unilearn.user.domain.SignUpForm;
import unilearn.unilearn.user.domain.User;
import unilearn.unilearn.user.jwt.JwtTokenProvider;
import unilearn.unilearn.user.repository.UserRepository;

import java.util.Collection;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    //
    public User processNewUser(SignUpForm signUpForm){
        // TODO send email and confirm
        return saveNewUser(signUpForm);
    }

    private User saveNewUser(SignUpForm signUpForm){
        User user = User.builder()
                .loginId(signUpForm.getLoginId())
                .email(signUpForm.getEmail())
                .username(signUpForm.getUsername())
                .nickname(signUpForm.getNickname())
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
        return userRepository.save(user);
    }
}
