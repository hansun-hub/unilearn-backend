package unilearn.unilearn.studyDetailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unilearn.unilearn.assignmentsSubmitPosts.entity.ViewTemperatureDto;
import unilearn.unilearn.user.entity.Temperature;
import unilearn.unilearn.user.entity.User;
import unilearn.unilearn.user.repository.TemperatureRepository;
import unilearn.unilearn.user.repository.UserRepository;

@Service
public class TemperatureService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TemperatureRepository temperatureRepository;
    public ViewTemperatureDto viewTemperature(Long user_id){
        User user = userRepository.findById(user_id)
                .orElse(null);
        Temperature temperature = temperatureRepository.findTemperatureByUser(user);
        if (user == null) {
            throw new RuntimeException("User not found with id: " + user_id);
        }

        return ViewTemperatureDto.builder()
                .user_id(user_id)
                .temperature(temperature.getTemperature())
                .build();

    }
}
