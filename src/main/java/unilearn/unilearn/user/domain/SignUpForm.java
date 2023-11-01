package unilearn.unilearn.user.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import org.springframework.web.bind.annotation.GetMapping;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter @Setter
public class SignUpForm {
    @NotNull(message = "아이디를 입력해주세요.")
    private String loginId;
    @NotNull(message = "비밀번호를 입력해주세요.")
    private String password;
    @NotNull(message = "이메일을 입력해주세요.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@(swu\\.ac\\.kr)$", message = "이메일 형식이 올바르지 않습니다. ")
    private String email;
    @NotNull(message = "이름을 입력해주세요.")
    private String username;
    @NotNull(message = "닉네임을 입력해주세요.")
    private String nickname;
}
