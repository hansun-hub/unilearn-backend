package unilearn.unilearn.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unilearn.unilearn.user.dto.MySubjectRequestDto;
import unilearn.unilearn.user.dto.MySubjectResponseDto;
import unilearn.unilearn.user.entity.MySubject;
import unilearn.unilearn.user.entity.User;
import unilearn.unilearn.user.repository.MySubjectRepository;
import unilearn.unilearn.user.repository.UserRepository;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MySubjectService {
    private final MySubjectRepository mySubjectRepository;
    private final UserRepository userRepository;

    public MySubjectResponseDto.MySubjectResponseForm createMySubject(MySubjectRequestDto.MySubjectRequestForm form, Principal principal) {
        User user = userRepository.findByNickname(principal.getName());
        MySubject mySubject = MySubject.builder()
                .user(user)
                .department(form.getDepartment())
                .year(form.getYear())
                .semester(form.getSemester())
                .professor(form.getProfessor())
                .sbjName(form.getSbj_name())
                .build();
        MySubject save = mySubjectRepository.save(mySubject);
        return MySubjectResponseDto.MySubjectResponseForm.builder().MySubjectId(save.getId()).build();
    }

    public MySubjectResponseDto.MySubjectResponseForm deleteMySubject(Long mySbjId, Principal principal) {
        User user = userRepository.findByNickname(principal.getName());
        MySubject mySbj = mySubjectRepository.findById(mySbjId).orElseThrow(() -> new NullPointerException(mySbjId+" 를 아이디로 갖는 수강과목이 존재하지 않습니다."));
        if (mySbj.getUser() != user){
            return null ;
        }
        mySubjectRepository.delete(mySbj);
        return MySubjectResponseDto.MySubjectResponseForm.builder().MySubjectId(mySbj.getId()).build();
    }

    private MySubjectResponseDto.MySubjectResponseListForm toMySubjectResponseListForm(MySubject sbj){
        return MySubjectResponseDto.MySubjectResponseListForm.builder()
                .courseId(sbj.getId())
                .sbj_name(sbj.getSbjName())
                .department(sbj.getDepartment())
                .year(sbj.getYear())
                .semester(sbj.getSemester())
                .professor(sbj.getProfessor())
                .build();
    }

    public List<MySubjectResponseDto.MySubjectResponseListForm> list(Principal principal) {
        User user = userRepository.findByNickname(principal.getName());
        List<MySubject> mySubjectList = mySubjectRepository.findAllByUser(user);
        if (mySubjectList.isEmpty()){
            return null ;
        }
        return mySubjectList.stream()
                .map(mySbj -> toMySubjectResponseListForm(mySbj))
                .collect(Collectors.toList());
    }
}
