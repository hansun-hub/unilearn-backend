package unilearn.unilearn.studyDetailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unilearn.unilearn.assignmentsPosts.entity.AssignmentsPosts;
import unilearn.unilearn.assignmentsSubmitPosts.entity.AssignmentsSubmitPosts;
import unilearn.unilearn.assignmentsSubmitPosts.entity.SubmitDetailDto;
import unilearn.unilearn.assignmentsSubmitPosts.entity.SubmitListDto;
import unilearn.unilearn.studyDetailRepository.AssignmentSubmitPostRepository;

import java.util.List;
import java.util.stream.Collectors;

// 과제제출 게시글 서비스
@Service
public class AssignmentSubmitPostService {

    @Autowired
    private AssignmentSubmitPostRepository assignmentSubmitPostRepository;
    public List<SubmitListDto> submitListGet (AssignmentsPosts assignmentsPosts) {
        List<AssignmentsSubmitPosts> assignmentsSubmitPosts = assignmentSubmitPostRepository.findByAssignmentsPosts(assignmentsPosts);
        return assignmentsSubmitPosts.stream()
                .map(this::submitList)
                .collect(Collectors.toList());
    }

    public SubmitDetailDto submitDetailGet (AssignmentsSubmitPosts assignmentsSubmitPosts){
        return SubmitDetailDto.builder()
                .assignment_name(assignmentsSubmitPosts.getAssignmentsPosts().getTitle())//과제 제목
                .submit_time(assignmentsSubmitPosts.getCreatedAt())//제출일시
                .name(assignmentsSubmitPosts.getUser().getNickname())//제출자명
                .content(assignmentsSubmitPosts.getContent())
                .submit_post_id(assignmentsSubmitPosts.getId())
                .build();

    }


    public SubmitListDto submitList(AssignmentsSubmitPosts assignmentsSubmitPosts) {
        return SubmitListDto.builder()
                .submit_post_id(assignmentsSubmitPosts.getId())
                .name(assignmentsSubmitPosts.getUser().getNickname())
                .submit_time(assignmentsSubmitPosts.getCreatedAt())
                .total_score(assignmentsSubmitPosts.getTotal_score())
                .feedback(assignmentsSubmitPosts.getFeedback())
                .build();
    }


    public AssignmentsSubmitPosts createSubmitPost(AssignmentsSubmitPosts entity) {
            return assignmentSubmitPostRepository.save(entity);
        }

    public AssignmentsSubmitPosts updateSubmit(Long postId, AssignmentsSubmitPosts updatedPost) {
        //과제제출 게시글 수정
            // 추가적인 로직 (예: 수정 권한 체크 등)이 필요하다면 여기에 추가
            AssignmentsSubmitPosts existingPost = assignmentSubmitPostRepository.findById(postId)
                    .orElseThrow(() -> new RuntimeException("Assignment Post not found with id: " + postId));

            // 업데이트할 필드가 null이 아닌 경우에만 업데이트
            if (updatedPost.getContent() != null) {
                existingPost.setContent(updatedPost.getContent());
            }


            return assignmentSubmitPostRepository.save(existingPost);
        }


}

