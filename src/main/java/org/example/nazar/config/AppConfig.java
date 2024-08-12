package org.example.nazar.config;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan({"org.example.nazar.*.*.*.*"})
@EnableAspectJAutoProxy
public class AppConfig {

}
