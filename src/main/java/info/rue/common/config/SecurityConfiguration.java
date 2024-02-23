package info.rue.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 스프링 부트 3.1기준으로 authorizeHttpRequests() 메소드가 deprecated 되었다.

        // 인가(접근권한) 설정
        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/**").permitAll()
        );
/*
        // 사이트 위변조 요청 방지
        http.csrf().disable();

        // 로그인 설정
        http.formLogin()
                .loginPage("/user2/login")
                .defaultSuccessUrl("/user2/loginSuccess")
                .failureUrl("/user2/login?success=100)")
                .usernameParameter("uid")
                .passwordParameter("pass");

        // 로그아웃 설정
        http.logout()
                .invalidateHttpSession(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/user2/logout"))
                .logoutSuccessUrl("/user2/login?success=200");

        // 사용자 인증 처리 컴포넌트 서비스 등록
        http.userDetailsService(service);
 */
        return http.build();
    }

/*
    @Bean
    public PasswordEncoder PasswordEncoder () {
        //return new MessageDigestPasswordEncoder("SHA-256");
        return new BCryptPasswordEncoder();
    }
*/

}
