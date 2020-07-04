package com.paymybuddy.payapp.config;

import com.paymybuddy.payapp.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder bcryptEncoder;
    private final PersistentTokenRepository jdbcTokenRepository;

    @Autowired
    public SecurityConfiguration(@Qualifier("userCredentialsService") UserDetailsService userDetailsService,
                                 PasswordEncoder passwordEncoder,
                                 PersistentTokenRepository persistentTokenRepository) {
        this.userDetailsService = userDetailsService;
        this.bcryptEncoder = passwordEncoder;
        this.jdbcTokenRepository = persistentTokenRepository;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bcryptEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/registration").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/swagger-ui*").hasAuthority(Role.ADMIN.name())
                .antMatchers("/api-docs*").hasAuthority(Role.ADMIN.name())
                .antMatchers(HttpMethod.GET).authenticated()
                .antMatchers(HttpMethod.PUT).authenticated()
                .antMatchers(HttpMethod.POST).authenticated()
                .antMatchers(HttpMethod.DELETE).authenticated()

                .and()
                .formLogin()
                .successForwardUrl("/user")

                .and()
                .csrf().ignoringAntMatchers("/registration")

                .and()
                .rememberMe().tokenRepository(jdbcTokenRepository);
    }

    @Profile("dev")
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/h2-console/**");
    }

}
