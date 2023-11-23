package unilearn.unilearn.studyDetailController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import unilearn.unilearn.assignmentsPosts.entity.AssignmentListDto;
import unilearn.unilearn.assignmentsPosts.entity.AssignmentsPosts;
import unilearn.unilearn.assignmentsPosts.entity.AssignmentsPostsDto;
import unilearn.unilearn.assignmentsSubmitPosts.entity.AssignmentsSubmitPosts;
import unilearn.unilearn.assignmentsSubmitPosts.entity.AssignmentsSubmitPostsDto;
import unilearn.unilearn.assignmentsSubmitPosts.entity.SubmitDetailDto;
import unilearn.unilearn.assignmentsSubmitPosts.entity.SubmitListDto;
import unilearn.unilearn.evaluation.entity.Evaluation;
import unilearn.unilearn.evaluation.entity.EvaluationDto;
import unilearn.unilearn.studyDetailRepository.AssignmentPostRepository;
import unilearn.unilearn.studyDetailRepository.AssignmentSubmitPostRepository;
import unilearn.unilearn.studyDetailRepository.EvaluationRepository;
import unilearn.unilearn.studyDetailService.AssignmentPostService;
import unilearn.unilearn.studyDetailService.AssignmentSubmitPostService;
import unilearn.unilearn.user.entity.Temperature;
import unilearn.unilearn.user.entity.User;
import unilearn.unilearn.user.repository.TemperatureRepository;
import unilearn.unilearn.user.repository.UserRepository;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/assignments-posts")
public class AssignmentPostController {

    @Autowired
    private AssignmentPostService assignmentPostService;
    @Autowired
    private AssignmentSubmitPostService assignmentSubmitPostService;

    private final UserRepository userRepository;
    private final AssignmentPostRepository assignmentPostRepository;
    private final AssignmentSubmitPostRepository assignmentSubmitPostRepository;
    private  final EvaluationRepository evaluationRepository;
    private  final TemperatureRepository temperatureRepository;

    //과제 게시글 전체 조회 - 마감전
    @GetMapping(params = "status=before")
    public ResponseEntity<List<AssignmentListDto>> getAllAssignmentPostsBeforeDeadline(Principal principal) {
        if (principal.getName() != null) {
            System.out.println(principal.getName() + principal);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        User user = userRepository.findByNickname(principal.getName());
        log.info("principal user = " + user.toString());
        List<AssignmentListDto> assignmentListDtos = assignmentPostService.getAllAssignmentPostsBeforeDeadline();

        return new ResponseEntity<>(assignmentListDtos, HttpStatus.OK);
    }

    //과제 게시글 전체 조회 -마감후
    @GetMapping(params = "status=after")
    public ResponseEntity<List<AssignmentListDto>> getAllAssignmentPostsAfterDeadline(Principal principal) {
        if (principal.getName() != null) {
            System.out.println(principal.getName() + principal);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        User user = userRepository.findByNickname(principal.getName());
        log.info("principal user = " + user.toString());
        List<AssignmentListDto> assignmentListDtos = assignmentPostService.getAllAssignmentPostsAfterDeadline();
        return new ResponseEntity<>(assignmentListDtos, HttpStatus.OK);
    }

    //과제 게시글 생성
    @PostMapping
    public ResponseEntity<Long> createAssignmentPost(@RequestBody AssignmentsPostsDto dto, BindingResult bindingResult, Principal principal) {
        if (principal.getName() != null) {
            System.out.println(principal.getName() + principal);
        } else if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        User user = userRepository.findByNickname(principal.getName());
        log.info("principal user = " + user.toString());
        AssignmentsPosts entity = new AssignmentsPosts();
        entity.setUser(user);
        entity.setTitle(dto.getTitle());
        entity.setDeadline(dto.getDeadline());
        AssignmentsPosts createdPost = assignmentPostService.createAssignmentPost(entity);
        return new ResponseEntity<>(createdPost.getId(), HttpStatus.CREATED);
    }

    //과제 게시글 수정
    @PatchMapping("/{assignments-posts_id}")
    public ResponseEntity<Long> updateAssignmentPost(
            @PathVariable("assignments-posts_id") Long postId,
            @RequestBody AssignmentsPosts updatedPost, BindingResult bindingResult, Principal principal) {
        if (principal.getName() != null) {
            System.out.println(principal.getName() + principal);
        } else if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        User user = userRepository.findByNickname(principal.getName());
        log.info("principal user = " + user.toString());
        AssignmentsPosts updatedAssignmentPost = assignmentPostService.updateAssignmentPost(postId, updatedPost);
        return new ResponseEntity<>(updatedAssignmentPost.getId(), HttpStatus.OK);
    }

    // 과제에 대한 과제 제출 게시글 작성
    @PostMapping("/{assignments-posts-id}/submit")
    public ResponseEntity<Long> submitAssignment(
            @PathVariable("assignments-posts-id") Long assignmentsPostsId,
            @RequestBody AssignmentsSubmitPostsDto dto,
            BindingResult bindingResult, Principal principal) {

        if (principal.getName() != null) {
            System.out.println(principal.getName() + principal);
        } else if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        User user = userRepository.findByNickname(principal.getName());
        log.info("principal user = " + user.toString());


        // 과제 제출 게시글 생성 및 저장
        AssignmentsPosts assignmentsPosts = assignmentPostRepository.findById(assignmentsPostsId)
                .orElseThrow(() -> new RuntimeException("Assignment Post not found with id: " + assignmentsPostsId));
        //Study study = assignmentsPosts.getStudy();


        AssignmentsSubmitPosts entity = new AssignmentsSubmitPosts();
        entity.setAssignmentsPosts(assignmentsPosts);
        //entity.setStudy(study);
        entity.setUser(user);
        entity.setContent(dto.getContent());


        AssignmentsSubmitPosts createdSubmitPost = assignmentSubmitPostService.createSubmitPost(entity);
        //제출인원 +1
        assignmentsPosts.setSubmit_num(assignmentsPosts.getSubmit_num()+1);
        //제출자 추가
        assignmentsPosts.setSubmit_name(assignmentsPosts.getSubmit_name()+" "+user.getNickname());
        assignmentPostService.updateAssignmentPost(assignmentsPostsId,assignmentsPosts);
        return new ResponseEntity<>(createdSubmitPost.getId(), HttpStatus.CREATED);
    }

    //과제제출 게시글 수정
    @PatchMapping({"/{assignments-posts-id}/submit/{assignments_submit_posts_id}"})
    public ResponseEntity<Long> submitAssignmentPost(
            @PathVariable("assignments_submit_posts_id") Long postId,
            @RequestBody AssignmentsSubmitPosts updatedPost, BindingResult bindingResult, Principal principal) {
        if (principal.getName() != null) {
            System.out.println(principal.getName() + principal);
        } else if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        User user = userRepository.findByNickname(principal.getName());
        log.info("principal user = " + user.toString());
        AssignmentsSubmitPosts updatedAssignmentSubmitPost = assignmentSubmitPostService.updateSubmit(postId, updatedPost);
        return new ResponseEntity<>(updatedAssignmentSubmitPost.getId(), HttpStatus.OK);
    }

    //과제 제출 게시글 마감후에 해당과제에 대한 과제제출 전체 리스트 보기
    @GetMapping({"/{assignments-posts-id}/submit"})
    public ResponseEntity<List<SubmitListDto>> getAllAssignmentSubmitPost(
            @PathVariable("assignments-posts-id") Long postId,Principal principal) {
        if (principal.getName() != null){
            System.out.println(principal.getName() + principal);
        }

        else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        User user = userRepository.findByNickname(principal.getName());
        log.info("principal user = " + user.toString());
        Optional<AssignmentsPosts> optionalAssignmentsPosts = assignmentPostRepository.findById(postId);
        if (optionalAssignmentsPosts.isEmpty()) {
            // ID에 해당하는 엔티티가 없을 때의 처리
            System.err.println("AssignmentsPosts with ID " + postId + " not found.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        AssignmentsPosts assignmentsPosts = optionalAssignmentsPosts.get();

        List<SubmitListDto> submitListDtos = assignmentSubmitPostService.submitListGet(assignmentsPosts);
        return new ResponseEntity<>(submitListDtos, HttpStatus.OK);
    }

    //특정 과제제출 게시글 상세보기
    @GetMapping({"/{assignments-posts-id}/submit/{assignments_submit_posts_id}"})
    public ResponseEntity<SubmitDetailDto> getAssignmentSubmitPostDetail(
            @PathVariable("assignments_submit_posts_id") Long postId,Principal principal) {
        if (principal.getName() != null) {
            System.out.println(principal.getName() + principal);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        User user = userRepository.findByNickname(principal.getName());
        log.info("principal user = " + user.toString());
        AssignmentsSubmitPosts assignmentsSubmitPost =  assignmentSubmitPostRepository.findById(postId).orElse(null);
        SubmitDetailDto submitDetailDto = assignmentSubmitPostService.submitDetailGet(assignmentsSubmitPost);
        return new ResponseEntity<>(submitDetailDto, HttpStatus.OK);
    }

    //과제 평가 작성
    @PostMapping("/evaluate/{assignments-submit-posts-id}")
    public ResponseEntity<Long> evaluateAssignment(
            @PathVariable("assignments-submit-posts-id") Long assignmentsSubmitPostsId,
            @RequestBody EvaluationDto evaluationDto,
            BindingResult bindingResult, Principal principal) {
        if (principal.getName() != null) {
            System.out.println(principal.getName() + principal);
        } else if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        User user = userRepository.findByNickname(principal.getName());
        log.info("principal user = " + user.toString());

        AssignmentsSubmitPosts assignmentsSubmitPosts = assignmentSubmitPostRepository.findById(assignmentsSubmitPostsId)
                .orElseThrow(() -> new RuntimeException("Assignments Submit Post not found with id: " + assignmentsSubmitPostsId));

        // 이미 평가를 작성했는지 확인
        Optional<Evaluation> existingEvaluation = evaluationRepository.findByAssignmentsSubmitPostsAndUser(assignmentsSubmitPosts, user);
        if (existingEvaluation.isPresent()) {
            // 이미 평가를 작성한 경우
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // 평가 작성
        Evaluation evaluation = new Evaluation();
        evaluation.setAssignmentsSubmitPosts(assignmentsSubmitPosts);
        evaluation.setUser(user);
        evaluation.setFeedback(evaluationDto.getFeedback());
        evaluation.setSurvey1(evaluationDto.getSurvey1());
        evaluation.setSurvey2(evaluationDto.getSurvey2());
        evaluation.setSurvey3(evaluationDto.getSurvey3());
        evaluation.setSurvey4(evaluationDto.getSurvey4());
        evaluation.setSurvey5(evaluationDto.getSurvey5());
        evaluation.setTotal_score(evaluationDto.getSurvey1() + evaluationDto.getSurvey2() + evaluationDto.getSurvey3() + evaluationDto.getSurvey4() + evaluationDto.getSurvey5());
        evaluation.setCreatedAt(LocalDateTime.now());

        // Evaluation 저장
        Evaluation savedEvaluation = evaluationRepository.save(evaluation);
        assignmentsSubmitPosts.setTotal_score(assignmentsSubmitPosts.getTotal_score()+evaluation.getTotal_score());
        assignmentsSubmitPosts.setFeedback(assignmentsSubmitPosts.getFeedback()+" "+evaluation.getFeedback());
        assignmentSubmitPostService.updateSubmit(assignmentsSubmitPostsId, assignmentsSubmitPosts);
        //total score 1~5이므로 0.1씩 반영하면 될 듯
        User target_user = assignmentsSubmitPosts.getUser();
        Temperature temperature = temperatureRepository.findTemperatureByUser(target_user);
        temperature.setTemperature(temperature.getTemperature()+(double)evaluation.getTotal_score()/10);
        temperatureRepository.save(temperature);//온도 변경사항 저장
        return new ResponseEntity<>(savedEvaluation.getId(), HttpStatus.CREATED);
    }




}
