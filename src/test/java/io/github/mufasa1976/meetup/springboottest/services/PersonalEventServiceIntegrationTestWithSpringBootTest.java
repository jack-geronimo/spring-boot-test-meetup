package io.github.mufasa1976.meetup.springboottest.services;

import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.jpa.show-sql=true")
@AutoConfigureDataJpa
@Transactional
public class PersonalEventServiceIntegrationTestWithSpringBootTest extends AbstractPersonalEventServiceIntegrationTest {}
