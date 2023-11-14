package unilearn.unilearn.user.config;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import unilearn.unilearn.user.entity.User;

import java.util.List;

public class UserSecurityAdapter extends org.springframework.security.core.userdetails.User {
    private User user;

    public UserSecurityAdapter(User user) {
        super(user.getNickname(), user.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
        this.user = user;
    }
}