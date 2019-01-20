package io.github.mufasa1976.meetup.springboottest.representation.rest;

import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.jpa.show-sql=true")
@AutoConfigureMockMvc
@AutoConfigureDataJpa
public class PersonalEventRestControllerIntegrationTestWithSpringBootTest extends AbstractPersonalEventRestControllerIntegrationTest {}
