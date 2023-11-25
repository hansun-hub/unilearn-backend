/*package unilearn.unilearn.subject.entity;

import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.runner.RunWith;
import unilearn.unilearn.subject.repository.SubjectRepository;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SubjectTest {

    @LocalServerPort
    private int port;

    @Autowired
    private SubjectRepository subjectRepository;

    @After
    public void clean() {
        subjectRepository.deleteAll();
    }

    @Test
    public void 개설과목_조회() throws Exception{
        //given
        String department = "정보보호";
        String professor = "김명주";
        int year = 2023;
        int semester = 1;


        //when

        //then
    }
}*/