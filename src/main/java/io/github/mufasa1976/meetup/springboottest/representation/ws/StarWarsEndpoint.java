package io.github.mufasa1976.meetup.springboottest.representation.ws;

import io.github.mufasa1976.meetup.springboottest.domains.StarWarsPerson;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.validation.constraints.NotEmpty;
import javax.xml.bind.annotation.XmlElement;

@WebService(targetNamespace = "http://springboottest-meetup.mufasa1976.github.io", name = "StarWarsEndpoint")
public interface StarWarsEndpoint {
  @WebMethod(operationName = "getPerson")
  @WebResult
  StarWarsPerson getPerson(@WebParam(name = "id") @XmlElement(required = true) @NotEmpty String id);
}
