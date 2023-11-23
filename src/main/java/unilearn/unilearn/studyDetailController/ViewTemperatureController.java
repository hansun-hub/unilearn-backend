package unilearn.unilearn.studyDetailController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import unilearn.unilearn.assignmentsPosts.domain.AssignmentsPosts;
import unilearn.unilearn.assignmentsSubmitPosts.domain.ViewTemperatureDto;
import unilearn.unilearn.studyDetailService.AssignmentPostService;
import unilearn.unilearn.studyDetailService.TemperatureService;
import unilearn.unilearn.user.entity.User;
import unilearn.unilearn.user.repository.UserRepository;

import java.security.Principal;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/{user_id}/temperature")
public class ViewTemperatureController {
    @Autowired
    private TemperatureService temperatureService;
    private  final UserRepository userRepository;
    @GetMapping
    public ResponseEntity<ViewTemperatureDto> userTemperature (@PathVariable("user_id") Long user_id, Principal principal){
        if (principal.getName() != null) {
            System.out.println(principal.getName() + principal);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        User user = userRepository.findByNickname(principal.getName());
        log.info("principal user = " + user.toString());
        ViewTemperatureDto viewTemperatureDto = temperatureService.viewTemperature(user_id);
        return new ResponseEntity<>(viewTemperatureDto, HttpStatus.OK);
    }
}