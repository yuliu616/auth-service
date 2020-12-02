package com.yu;

import com.yu.filter.JwtTokenBasedSecurityFilter;
import com.yu.model.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${authService.apiBaseUrl}")
    protected String apiBaseUrl;

    @Value("${authService.options.enableDebugEndpoint}")
    protected boolean enableDebugEndpoint;

    private static final Logger logger = LoggerFactory.getLogger(AppSecurityConfig.class);

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // ensure the service is completely stateless (and never set cookie)
        http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.csrf().disable();

        // access control for debug endpoints
        if (this.enableDebugEndpoint) {
            http.authorizeRequests()
                    .antMatchers(apiBaseUrl+"/debug/**").permitAll();
        }

        // access control for endpoints
        http
            .authorizeRequests()
                .antMatchers(apiBaseUrl+"/about")
                    .permitAll()
                .antMatchers(apiBaseUrl+"/signUp")
                    .anonymous()
                .antMatchers(apiBaseUrl+"/login")
                    .permitAll()
                .antMatchers(apiBaseUrl+"/login/aboutMe")
                    .authenticated()
                .antMatchers(apiBaseUrl+"/login/refreshToken")
                    .authenticated()
                .antMatchers(apiBaseUrl+"/role/*/bind")
                    .hasAnyAuthority(Role.ROOT_ADMIN.name(), Role.ROLE_ADMIN.name())
                .antMatchers(apiBaseUrl+"/role/*/unbind")
                    .hasAnyAuthority(Role.ROOT_ADMIN.name(), Role.ROLE_ADMIN.name())
                .anyRequest().denyAll()
        ;
//        http.authorizeRequests().anyRequest().permitAll();

        http.addFilterBefore(new JwtTokenBasedSecurityFilter(), UsernamePasswordAuthenticationFilter.class);
    }

}
