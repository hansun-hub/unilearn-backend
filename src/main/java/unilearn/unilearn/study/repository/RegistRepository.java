package unilearn.unilearn.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unilearn.unilearn.study.entity.Regist;
import unilearn.unilearn.study.entity.Study;

import java.util.List;

public interface RegistRepository  extends JpaRepository<Regist, Long> {
    List<Regist> findByStudy(Study study);
}
