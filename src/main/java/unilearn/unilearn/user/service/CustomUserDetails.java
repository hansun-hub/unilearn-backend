package unilearn.unilearn.user.service;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import unilearn.unilearn.user.entity.School;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {
    private Long id;
    private String loginId;
    private String email;
    private boolean emailChecked;
    private String checkEmailToken;
    private String username;
    private String password;
    private String nickname;
    private School school;
    private List<String> roles;

    public CustomUserDetails(Long id, String loginId, String email, boolean emailChecked, String checkEmailToken, String username, String password, String nickname, School school, List<String> roles) {
        this.id = id;
        this.loginId = loginId;
        this.email = email;
        this.emailChecked = emailChecked;
        this.checkEmailToken = checkEmailToken;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.school = school;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return loginId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
