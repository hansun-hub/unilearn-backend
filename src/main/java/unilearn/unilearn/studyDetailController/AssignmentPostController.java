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
import unilearn.unilearn.assignmentsSubmitPosts.entity.*;
import unilearn.unilearn.evaluation.entity.Evaluation;
import unilearn.unilearn.evaluation.entity.EvaluationDto;
import unilearn.unilearn.study.controller.StudyController;
import unilearn.unilearn.study.entity.Study;
import unilearn.unilearn.study.repository.StudyRepository;
import unilearn.unilearn.studyDetailRepository.AssignmentPostRepository;
import unilearn.unilearn.studyDetailRepository.AssignmentSubmitPostRepository;
import unilearn.unilearn.studyDetailRepository.EvaluationRepository;
import unilearn.unilearn.studyDetailService.AssignmentPostService;
import unilearn.unilearn.studyDetailService.AssignmentSubmitPostService;
import unilearn.unilearn.user.entity.Temperature;
import unilearn.unilearn.user.entity.User;
import unilearn.unilearn.user.exception.StudyNotFoundException;
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
    private  final StudyRepository studyRepository;
    private final AssignmentSubmitPostRepository assignmentSubmitPostRepository;
    private  final EvaluationRepository evaluationRepository;
    private  final TemperatureRepository temperatureRepository;

    //특정 스터디의 과제 게시글 전체 조회 - 마감전
    @GetMapping(value = "/{study_id}",params = "status=before")
    public ResponseEntity<List<AssignmentListDto>> getAllAssignmentPostsBeforeDeadline(@PathVariable("study_id") Long study_id, Principal principal) {
        if (principal.getName() != null) {
            System.out.println(principal.getName() + principal);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        User user = userRepository.findByNickname(principal.getName());
        log.info("principal user = " + user.toString());
        List<AssignmentListDto> assignmentListDtos = assignmentPostService.getAllAssignmentPostsBeforeDeadline(study_id);

        return new ResponseEntity<>(assignmentListDtos, HttpStatus.OK);
    }

    //특정 스터디의 과제 게시글 전체 조회 -마감후
    @GetMapping(value = "/{study_id}",params = "status=after")
    public ResponseEntity<List<AssignmentListDto>> getAllAssignmentPostsAfterDeadline(@PathVariable("study_id") Long study_id, Principal principal) {
        if (principal.getName() != null) {
            System.out.println(principal.getName() + principal);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        User user = userRepository.findByNickname(principal.getName());
        log.info("principal user = " + user.toString());
        List<AssignmentListDto> assignmentListDtos = assignmentPostService.getAllAssignmentPostsAfterDeadline(study_id);
        return new ResponseEntity<>(assignmentListDtos, HttpStatus.OK);
    }

    //과제 게시글 생성
    @PostMapping("/{study_id}")
    public ResponseEntity<Long> createAssignmentPost(@PathVariable("study_id") Long studyId, @RequestBody AssignmentsPostsDto dto, BindingResult bindingResult, Principal principal) {
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
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> {
                    log.error("Study not found with id: " + studyId);
                    return new StudyNotFoundException("Study not found with id: " + studyId);
                });
        entity.setStudy(study);
        AssignmentsPosts createdPost = assignmentPostService.createAssignmentPost(entity);
        return new ResponseEntity<>(createdPost.getId(), HttpStatus.CREATED);
    }

    //과제 게시글 수정
    @PatchMapping("edit-post/{assignments-posts_id}")
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
        AssignmentsPosts assignmentstPosts = assignmentPostRepository.findById(postId).orElse(null);
        User postUser = assignmentstPosts.getUser();
        if (user.equals(postUser)) {
            AssignmentsPosts updatedAssignmentPost = assignmentPostService.updateAssignmentPost(postId, updatedPost);
            return new ResponseEntity<>(updatedAssignmentPost.getId(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }



    // 과제에 대한 과제 제출 게시글 작성
    @PostMapping("/{assignments-posts-id}/submit")
    public ResponseEntity<Long> submitAssignment(
            @PathVariable("assignments-posts-id") Long assignmentsPostsId,
            @ModelAttribute AssignmentsSubmitPostsDto dto,
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
        if (assignmentSubmitPostService.hasUserSubmittedAssignment(user, assignmentsPosts)) {
            return new  ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        AssignmentsSubmitPosts entity = new AssignmentsSubmitPosts();
        entity.setAssignmentsPosts(assignmentsPosts);
        //entity.setStudy(study);
        entity.setUser(user);
        entity.setContent(dto.getContent());
        entity.setImg(dto.getImg());

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
            @ModelAttribute AssignmentsSubmitPostsDto updatedPost, BindingResult bindingResult, Principal principal) {
        if (principal.getName() != null) {
            System.out.println(principal.getName() + principal);
        } else if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        User user = userRepository.findByNickname(principal.getName());
        log.info("principal user = " + user.toString());
        AssignmentsSubmitPosts assignmentsSubmitPosts = assignmentSubmitPostRepository.findById(postId).orElse(null);
        User postUser = assignmentsSubmitPosts.getUser();
        if (user.equals(postUser)) {
            assignmentSubmitPostService.updateSubmit(postId, updatedPost);
            return new ResponseEntity<>(postId, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }

    //특정 과제 내가 쓴 과제 제출 게시글 상세보기
    @GetMapping("mysubmit/{assignments-posts-id}")
    public ResponseEntity<MySubmitDto> getMySubmitDetail(@PathVariable("assignments-posts-id") Long postId, Principal principal){
        if (principal.getName() != null){
            System.out.println(principal.getName() + principal);
        }

        else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        User user = userRepository.findByNickname(principal.getName());
        log.info("principal user = " + user.toString());
        MySubmitDto mySubmitDto = assignmentSubmitPostService.getMysubmit(postId,user);
        return new ResponseEntity<>(mySubmitDto, HttpStatus.OK);
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

        assignmentSubmitPostRepository.save(assignmentsSubmitPosts);

        //total score 1~5이므로 0.1씩 반영하면 될 듯
        User target_user = assignmentsSubmitPosts.getUser();
        Temperature temperature = temperatureRepository.findTemperatureByUser(target_user);
        temperature.setTemperature(temperature.getTemperature()+(double)evaluation.getTotal_score()/10);
        temperatureRepository.save(temperature);//온도 변경사항 저장
        return new ResponseEntity<>(savedEvaluation.getId(), HttpStatus.CREATED);
    }




}

