package sheridan.gahirl.assignment4.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import sheridan.gahirl.assignment4.services.UserDetailsServiceImpl;


@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	private LoggingAccessDeniedHandler accessDeniedHandler;


	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**").and().ignoring().antMatchers("/h2-console/**");
	}


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable(); 
		http.headers().frameOptions().disable();
		
		http.authorizeRequests()
		 .antMatchers("/admin/**").hasAnyRole("ADMIN","MEMBER")
		 .antMatchers("/h2-console/**").permitAll()
		 .antMatchers(HttpMethod.POST,"/register").permitAll()
		 .antMatchers(
				 "/",
				 "/js/**",
				 "/css/**",
				 "/images/**",
				 "/**").permitAll()
		 .anyRequest().authenticated()
		 .and()
		 .formLogin().loginPage("/login").permitAll()
		 .and()
        .logout()
        .invalidateHttpSession(true)
		 .clearAuthentication(true)
		 .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		 .logoutSuccessUrl("/login?logout")
		 .permitAll()
		 .and().exceptionHandling()
		 .accessDeniedHandler(accessDeniedHandler);
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
				.passwordEncoder(passwordEncoder());
				
	}
}
