package unilearn.unilearn.studyDetailController;

/* 과제제출 게시글 컨트롤러
@RestController
@RequestMapping("/api/assignments-submit-posts")
public class AssignmentSubmitPostController {

    @Autowired
    private AssignmentSubmitPostService assignmentSubmitPostService;


    @GetMapping
    public ResponseEntity<List<AssignmentsSubmitPosts>> getAllAssignmentSubmitPosts(
            @RequestParam("deadline_status") String deadlineStatus) {
        List<AssignmentsSubmitPosts> submitPosts;
        //마감전
        if ("before".equals(deadlineStatus)) {
            submitPosts = assignmentSubmitPostService.getAllAssignmentSubmitPostsBeforeDeadline();
        }
        //마감후
        else if ("after".equals(deadlineStatus)) {
            submitPosts = assignmentSubmitPostService.getAllAssignmentSubmitPostsAfterDeadline();
        }
        //예외
        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(submitPosts, HttpStatus.OK);
    }
}*/
