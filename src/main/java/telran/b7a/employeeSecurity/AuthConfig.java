package telran.b7a.employeeSecurity;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AuthConfig extends WebSecurityConfigurerAdapter {
	
	@Override
	public void configure(WebSecurity web) {
		web.ignoring()
		.antMatchers(HttpMethod.POST, "/cvbank/employee/register**");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic();
		http.csrf().disable();
		http.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.authorizeRequests()
			.antMatchers("/cvbank/employee/login**")
				.permitAll()
			.antMatchers("/cvbank/employee/{id}**")
				.access("#id == authentication.name")
			.anyRequest()
				.authenticated();
	}
}
