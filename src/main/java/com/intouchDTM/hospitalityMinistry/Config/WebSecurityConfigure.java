package com.intouchDTM.hospitalityMinistry.Config;

import com.intouchDTM.hospitalityMinistry.Jwt.JwtAuthenticationFilter;
import com.intouchDTM.hospitalityMinistry.Jwt.JwtProvider;
import com.intouchDTM.hospitalityMinistry.OAuth2.CookieAuthorizationRequestRepository;
import com.intouchDTM.hospitalityMinistry.OAuth2.OAuth2AuthenticationFailureHandler;
import com.intouchDTM.hospitalityMinistry.OAuth2.OAuth2AuthenticationSuccessHandler;
import com.intouchDTM.hospitalityMinistry.OAuth2.Service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class WebSecurityConfigure {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtProvider jwtTokenProvider;
    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //httpBasic, csrf, formLogin, rememberMe, logout, session disable
        http
                .cors(c -> c.configure(http))//////////////////
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .rememberMe(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        //요청에 대한 권한 설정
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/oauth2/**").permitAll()
                        .anyRequest().authenticated()
                );

        //oauth2Login
        http
                .oauth2Login(
                        oauth -> {
                            oauth.authorizationEndpoint(author -> author.baseUri("/oauth2/authorize"));
                            oauth.authorizedClientRepository(
                                    (OAuth2AuthorizedClientRepository) cookieAuthorizationRequestRepository);// 인증 요청을 cookie 에 저장
                            oauth.redirectionEndpoint(
                                    redirect -> redirect.baseUri("/oauth2/callback/*")); // 소셜 인증 후 redirect url
                            oauth.userInfoEndpoint(userService -> userService.userService(
                                    customOAuth2UserService));//userService()는 OAuth2 인증 과정에서 Authentication 생성에 필요한 OAuth2User 를 반환하는 클래스를 지정한다.// 회원 정보 처리
                            oauth.successHandler(oAuth2AuthenticationSuccessHandler);
                            oauth.failureHandler(oAuth2AuthenticationFailureHandler);
                        }
                );
        http
                .logout(
                        out -> {
                            out.clearAuthentication(true);
                            out.deleteCookies("JSESSIONID");
                        }
                );

        //jwt filter 설정
        http
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
