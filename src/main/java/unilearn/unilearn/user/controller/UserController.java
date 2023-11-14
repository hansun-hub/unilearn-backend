package unilearn.unilearn.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import unilearn.unilearn.user.dto.LoginForm;
import unilearn.unilearn.user.dto.SignUpForm;
import unilearn.unilearn.user.exception.UserNotValidException;
import unilearn.unilearn.user.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> submitSignUp(@Valid @RequestBody SignUpForm signUpForm, BindingResult bindingResult) {
        try{
            if (bindingResult.hasErrors()){
                List<String> errorList = userService.getSignUpErrorList(bindingResult);
                throw new UserNotValidException(errorList, "Custom Validator work");
            }
            else{
                return userService.getSuccessSignUpResponse(signUpForm);
            }
        }catch (Exception e) {
            log.info("잘못된 회원가입 양식입니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/login")
    public String login(@RequestBody LoginForm form){
        return userService.login(form);
    }
}
