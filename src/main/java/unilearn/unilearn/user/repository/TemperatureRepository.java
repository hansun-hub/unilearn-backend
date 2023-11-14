package unilearn.unilearn.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unilearn.unilearn.user.entity.Temperature;

@Repository
public interface TemperatureRepository extends JpaRepository<Temperature, Long> {
}
