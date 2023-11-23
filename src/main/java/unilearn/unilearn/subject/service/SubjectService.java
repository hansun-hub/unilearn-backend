package unilearn.unilearn.subject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unilearn.unilearn.subject.Dto.SubjectListResponseDto;
import unilearn.unilearn.subject.entity.Subject;
import unilearn.unilearn.subject.repository.SubjectRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;

    public Subject findById(Long id){
        return subjectRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 subject입니다."));
    }

    // 모든 과목 목록 반환
    @Transactional(readOnly = true)
    public List<SubjectListResponseDto> getAllSubjects() {
        return subjectRepository.findAllASC().stream()
                .map(SubjectListResponseDto::new)
                .collect(Collectors.toList());
    }
}
