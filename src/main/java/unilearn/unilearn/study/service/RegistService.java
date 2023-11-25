package unilearn.unilearn.study.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unilearn.unilearn.study.dto.RegistDto;
import unilearn.unilearn.study.entity.Regist;
import unilearn.unilearn.study.repository.RegistRepository;
import unilearn.unilearn.study.entity.Study;
import unilearn.unilearn.study.repository.StudyRepository;
import unilearn.unilearn.subject.entity.Subject;
import unilearn.unilearn.user.entity.User;
import unilearn.unilearn.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
public class RegistService {

    private final RegistRepository registRepository;
    private final StudyRepository studyRepository;
    private final UserRepository userRepository;

    @Autowired
    public RegistService(RegistRepository registRepository, StudyRepository studyRepository, UserRepository userRepository) {
        this.registRepository = registRepository;
        this.studyRepository = studyRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void applyForStudy(Regist regist) {
        registRepository.save(regist);
    }

}
