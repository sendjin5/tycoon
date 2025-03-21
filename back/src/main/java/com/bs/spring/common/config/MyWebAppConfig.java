package com.bs.spring.common.config;

import com.bs.spring.common.error.MyException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import java.util.List;
import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class MyWebAppConfig implements WebMvcConfigurer {

    private final LoginMemberArgumentResolver loginMemberArgumentResolver;

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        return messageSource;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
    }
////    @Bean
//    public HandlerExceptionResolver exceptionResolver() {
//        //에러페이지를 연결해 주는 Resolver
//        SimpleMappingExceptionResolver exceptionResolver
//                = new SimpleMappingExceptionResolver();
//
//        Properties prop = new Properties();
//        //key값으로 Exception명, value:연결주소설정
//        prop.put(RuntimeException.class.getName(), "/common/error/runtimeerror");
//        prop.put(MyException.class.getName(), "/common/error/myerror");
//
//        exceptionResolver.setExceptionMappings(prop);
//        //기본 에러처리페이지를 등록
//        //Exception이 발생하면, 에러기본페이지로 이동하도록 할 수 있다.
//        //Exception별로 분기하여 화면을 나눌수 있다.
//        exceptionResolver.setDefaultErrorView("/common/error/default");
//        return exceptionResolver;
//    }

    //crossorigin 허용하기

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                .allowedOrigins("http://localhost:5173","http://43.203.179.50","http://3.38.185.252/")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowCredentials(true);
//        registry.addMapping("/**")
//                .allowedOrigins("*");
    }


  /*  @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(1024*1024*100);
        return multipartResolver;
    }*/

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }


}
