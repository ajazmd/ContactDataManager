package com.smart.securityconfig;



import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
@Configuration
@EnableWebSecurity
public class MySecurityAdapter extends WebSecurityConfigurerAdapter{

	@Autowired
	private MyConfig config;

	@Autowired
	private DataSource dataSource;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(config.getDaoAuthenticationProvider());
	}
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/admin/**").hasRole("ADMIN")
		.antMatchers("/user/**").hasRole("USER")
		.antMatchers("/**").permitAll()
		.and()
		.formLogin()
		.loginPage("/login")
		.loginProcessingUrl("/dologin")
		.defaultSuccessUrl("/user/index")
		.failureUrl("/failure")
		.and()
		.rememberMe().tokenRepository(persistentTokenRepository())
		.and()
		.csrf().disable();
	}

	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl tokenRepo=new JdbcTokenRepositoryImpl();
		tokenRepo.setDataSource(dataSource);
		return tokenRepo;
	}
}
