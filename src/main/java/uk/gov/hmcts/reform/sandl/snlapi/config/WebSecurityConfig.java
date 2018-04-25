package uk.gov.hmcts.reform.sandl.snlapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@EnableWebSecurity
@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            .authorizeRequests()
                .antMatchers("/", "/login").permitAll()
            .and()
            .authorizeRequests()
                .antMatchers("/security").hasRole("USER")
                .antMatchers("/get-sessions").hasRole("USER")
            .and()
            .authorizeRequests()
                .anyRequest().authenticated()
            .and()
            .httpBasic()
            .and().csrf()
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .ignoringAntMatchers("/logout/**")
        ;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
        builder.inMemoryAuthentication()
            .withUser("officer1").password("asd").roles("USER", "OFFICER").and()
            .withUser("judge1").password("asd").roles("USER", "JUDGE").and()
            .withUser("admin").password("asd").roles("USER", "ADMIN");
    }

}
