package unilearn.unilearn.testService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import unilearn.unilearn.user.entity.User;
import unilearn.unilearn.user.repository.UserRepository;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PostContoller {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @PostMapping("/posts")
    public ResponseEntity<PostDto> create(@Valid @RequestBody PostDto postDto, BindingResult bindingResult, Principal principal) {
        if (principal.getName() != null){
            System.out.println(principal.getName() + principal);
        }
        else if(bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        User user = userRepository.findByNickname(principal.getName());
        log.info("principal user = " + user.toString());
        Post post = Post.builder()
                .user(user)
                .author(postDto.getAuthor())
                .body(postDto.getBody())
                .build();
        Post savePost = postRepository.save(post);

        PostDto returnDto = new PostDto();
        returnDto.setUser(savePost.getUser().getId());
        returnDto.setAuthor(savePost.getAuthor());
        returnDto.setBody(savePost.getBody());

        return ResponseEntity.status(HttpStatus.CREATED).body(returnDto);

    }
}
