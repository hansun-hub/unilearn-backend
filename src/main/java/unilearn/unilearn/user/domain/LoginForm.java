package unilearn.unilearn.user.domain;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class LoginForm {
    @NotNull(message = "아이디를 입력해주세요.")
    private String loginId;
    @NotNull(message = "비밀번호를 입력해주세요.")
    private String password;
}
