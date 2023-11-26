package unilearn.unilearn.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import unilearn.unilearn.user.dto.LoginForm;
import unilearn.unilearn.user.dto.SignUpForm;
import unilearn.unilearn.user.dto.UserDto;
import unilearn.unilearn.user.exception.UserNotValidException;
import unilearn.unilearn.user.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
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

    @GetMapping("/signup")
    public ResponseEntity<?> checkEmail(@RequestParam("email") String email, @RequestParam("code") String authCode){
        return userService.verifiedCode(email, authCode);
    }


    @GetMapping("/login")
    public ResponseEntity login(@RequestParam("loginId") String loginId, @RequestParam("password") String password){
        return userService.login(loginId, password);
    }

    @GetMapping("/api/user-detail")
    public ResponseEntity<UserDto.UserResponseDto> getUserProfile(Principal principal){
        if (principal.getName() == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UserDto.UserResponseDto responseDto = userService.getUserProfile(principal);
        if (responseDto == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PostMapping("/api/user-detail")
    public ResponseEntity updateUserDetail(@RequestBody @Valid UserDto.UserRequestDto form,
                                           BindingResult bindingResult, Principal principal) {
        if (principal.getName() == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        userService.updateUserProfile(form, principal);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
