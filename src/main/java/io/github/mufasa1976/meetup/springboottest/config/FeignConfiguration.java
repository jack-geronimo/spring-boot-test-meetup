package io.github.mufasa1976.meetup.springboottest.config;

import io.github.mufasa1976.meetup.springboottest.Application;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackageClasses = Application.class)
@EnableHystrix
public class FeignConfiguration {}
