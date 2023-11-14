package unilearn.unilearn.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import unilearn.unilearn.user.config.UserSecurityAdapter;
import unilearn.unilearn.user.entity.User;
import unilearn.unilearn.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {

        User user = userRepository.findByLoginId(loginId);
        if(user == null) throw new UsernameNotFoundException(String.format("USER : [%s]를 찾을 수 없습니다", loginId));

        return new UserSecurityAdapter(user);
    }
}