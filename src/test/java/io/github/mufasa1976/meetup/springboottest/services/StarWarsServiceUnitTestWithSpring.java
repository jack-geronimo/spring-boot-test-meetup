package io.github.mufasa1976.meetup.springboottest.services;

import io.github.mufasa1976.meetup.springboottest.cients.StarWarsClient;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = StarWarsServiceImpl.class)
public class StarWarsServiceUnitTestWithSpring extends AbstractStarWarsServiceUnitTest {
  @MockBean
  private StarWarsClient client;

  @Autowired
  private StarWarsService service;

  @Override
  protected StarWarsClient getClient() {
    return client;
  }

  @Override
  protected StarWarsService getService() {
    return service;
  }
}
