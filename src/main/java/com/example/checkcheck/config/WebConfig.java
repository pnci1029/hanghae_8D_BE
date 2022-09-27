package com.example.checkcheck.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    public static final String ALLOWED_METHOD_NAMES = "GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,PATCH";

    //    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .exposedHeaders("*")
//                .allowedHeaders("*")
////                .allowedMethods(ALLOWED_METHOD_NAMES.split(","))
//
//                .allowedMethods(
//                        HttpMethod.GET.name(),
//                        HttpMethod.POST.name(),
//                        HttpMethod.PATCH.name(),
//                        HttpMethod.DELETE.name(),
//                        HttpMethod.OPTIONS.name(),
//                        HttpMethod.HEAD.name(),
//                        HttpMethod.PUT.name())
////                .allowCredentials(true)
//                .maxAge(3600)
////                .allowedOrigins("https://devtools-gewfe9kn1-green9930.vercel.app,https://devtools-si83a57cz-green9930.vercel.app, http://localhost:3000");
//                .allowedOrigins("*");
//
//    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedOrigins("https://www.chackcheck99.com", "http://localhost:3000"+"\n")
                .allowedMethods("*");
    }

}
