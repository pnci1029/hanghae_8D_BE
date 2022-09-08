package com.example.checkcheck.config;


import com.example.checkcheck.security.JwtAuthenticationFilter;
import com.example.checkcheck.security.JwtExceptionFilter;
import com.example.checkcheck.security.JwtTokenProvider;
import com.example.checkcheck.security.test.JwtAccessDeniedHandler;
import com.example.checkcheck.security.test.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtExceptionFilter jwtExceptionFilter;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .antMatchers("/h2-console/**")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.cors().configurationSource(corsConfigurationSource());
        // 토큰 인증이므로 세션 사용x
        http.csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.headers().frameOptions().sameOrigin();

        http.authorizeRequests()
                // 회원 관리 처리 API 전부를 login 없이 허용

//                .antMatchers("/api/**").permitAll()
                .antMatchers("/user/**").permitAll()
                .antMatchers("/api/main/**").permitAll()
                .antMatchers("/auth/user/token").permitAll()


//                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()

                // 그 외 어떤 요청이든 '인증'
                .and()
//                .apply(new JwtSecurityConfig(jwtTokenProvider));
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class);
    }

//                    .antMatchers("/**").permitAll()

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
////        configuration.addAllowedOriginPattern("http://localhost:3000");
//        configuration.addAllowedOriginPattern("https://localhost:3000");
//        configuration.addAllowedOriginPattern("https://authex-d42a5.web.app/");
//        configuration.addAllowedOriginPattern("https://auth-6eb37.web.app");
//        configuration.addAllowedOriginPattern("https://test-react-basic.web.app");
//        configuration.addAllowedOriginPattern("https://authex-d42a5.web.app");
//        configuration.addAllowedOriginPattern("https://bungle.life");
//        configuration.addAllowedMethod("*");
//        configuration.addAllowedHeader("*");
//        configuration.addExposedHeader("Authorization");
//        configuration.addExposedHeader("RefreshToken");
//        configuration.setAllowCredentials(true);
////        configuration.validateAllowCredentials();
//        configuration.setMaxAge(3600L);
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
}