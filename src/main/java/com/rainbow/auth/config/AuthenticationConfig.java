//package com.rainbow.auth.config;
//
//import com.rainbow.auth.service.CustomUserDetailServiceImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Import;
//import org.springframework.security.config.annotation.ObjectPostProcessor;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.configuration.ObjectPostProcessorConfiguration;
//import org.springframework.security.core.userdetails.UserDetailsService;
//
///**
// * @author yanzhihao
// * @since 2022/3/21
// */
//@Configuration
//public class AuthenticationConfig {
//
//    @Autowired
//    private CustomUserDetailServiceImpl customUserDetailService;
//
//    @Bean
//    public AuthenticationManagerBuilder authenticationManagerBuilder(ObjectPostProcessor<Object> objectPostProcessor) {
//
//        try {
//            AuthenticationManagerBuilder authenticationManagerBuilder = new AuthenticationManagerBuilder(objectPostProcessor);
//            authenticationManagerBuilder.userDetailsService(customUserDetailService);
//            return authenticationManagerBuilder;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//}
