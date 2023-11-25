package unilearn.unilearn.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import unilearn.unilearn.study.entity.StdList;

import java.util.List;

@Repository
public interface StdListRepository extends JpaRepository<StdList, Long> {

    List<Long> findStudyIdsByUserId(Long userId);
}
