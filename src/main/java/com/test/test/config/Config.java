package com.test.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import javax.servlet.ServletContext;
import java.util.Collections;
import java.util.Properties;

@Configuration
public class Config {

    @Bean
    public BCryptPasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Docket swaggerConfiguration(ServletContext servletContext){

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.test.test"))
                .build()
                .apiInfo( setApiInfo());
    }

    public ApiInfo setApiInfo(){
        return new ApiInfo(
                "Backend App",
                "A simple REST API backend with spring boot.",
                "1.0",
                "Demo Purpose",
                new springfox.documentation.service.Contact("Adetunji Akinde","http://tijanja.com","adetunjiakinde@gmail.com"),
                "Public license",
                "http://tijanja.com/license",
                Collections.emptyList());
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("goodluckjonathan.estate@gmail.com");
        mailSender.setPassword("Pr0j3ct12e");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
