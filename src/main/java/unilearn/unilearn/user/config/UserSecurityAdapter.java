package unilearn.unilearn.user.config;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import unilearn.unilearn.user.domain.User;

import java.util.List;

// 여기서 상속받는 user는 springsecurity에서 기본 제공하는 유저
// 프로젝트에서는 새롭게 User엔티티 만들어서 사용합니다.
public class UserSecurityAdapter extends org.springframework.security.core.userdetails.User {
    private User user;

    public UserSecurityAdapter(User user) {
        super(user.getNickname(), user.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
        this.user = user;
    }
}