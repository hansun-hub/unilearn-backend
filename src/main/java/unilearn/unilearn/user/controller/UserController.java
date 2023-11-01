package unilearn.unilearn.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import unilearn.unilearn.user.domain.SignUpForm;
import unilearn.unilearn.user.exception.UserNotValidException;
import unilearn.unilearn.user.service.UserService;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<Map<String, String>> submitSignUp(@RequestBody SignUpForm signUpForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()){
            List<String> errorList = userService.getSignUpErrorList(bindingResult);
            throw new UserNotValidException(errorList, "Custom Validator work");
        }
        return userService.getSuccessSignUpResponse(signUpForm);
    }

    @GetMapping("/log-in")
    public String login(@RequestParam String loginId, @RequestParam String password){
        userService.login(loginId,password);
    }
}
