package io.github.mufasa1976.meetup.springboottest.services;

import io.github.mufasa1976.meetup.springboottest.cients.StarWarsClient;
import io.github.mufasa1976.meetup.springboottest.services.impl.StarWarsServiceImpl;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StarWarsServiceUnitTestWithoutSpring extends AbstractStarWarsServiceUnitTest {
  @Mock
  private StarWarsClient client;

  private StarWarsService service;

  @Before
  public void setUp() {
    service = new StarWarsServiceImpl(client);
  }

  @Override
  protected StarWarsClient getClient() {
    return client;
  }

  @Override
  protected StarWarsService getService() {
    return service;
  }
}
