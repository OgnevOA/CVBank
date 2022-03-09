package telran.b7a.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AuthConfig {
	
	@Configuration
	public static class CVConfigurerAdapter extends WebSecurityConfigurerAdapter {
		
		@Override
		public void configure(WebSecurity web) {
			web.ignoring()
			.antMatchers(HttpMethod.POST, "/cvbank/employee/register**", "/cvbank/employer/register**");
		}
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.httpBasic();
			http.csrf().disable();
			http.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
			http.authorizeRequests()
					//==================CV=================
				.antMatchers("/cvbank/employee/cv/add")
					.access("hasRole('EMPLOYEE')")
				.antMatchers("/cvbank/employee/cv/delete/{cvid}")
					.access("@customSecurity.checkCVAuthority(#cvid, authentication.name)")
				.antMatchers("/cvbank/employee/cv/{cvid}")
					.access("hasRole('EMPLOYER') or hasRole('ADMINISTRATOR') or @customSecurity.checkCVAuthority(#cvid, authentication.name)")
				.antMatchers("/cvbank/employee/cv/anonymise/{cvid}")
					.access("@customSecurity.checkCVAuthority(#cvid, authentication.name)")
					//==================EMPLOYEE=================
				.antMatchers(HttpMethod.POST, "/cvbank/employee/login")
					.authenticated()
				.antMatchers("/cvbank/employee/{id}")
					.access("#id == authentication.name")
					//==================EMPLOYER==================
				.antMatchers("/cvbank/employer/login")
					.authenticated()
				.antMatchers("/cvbank/employer/{companyId}")
					.access("@customSecurity.checkEmployeeCollectionAuthority(#companyId, authentication.name)")
				.antMatchers(HttpMethod.PUT, "/cvbank/employer/{companyId}/collection/{collectionName}")
					.access("@customSecurity.checkEmployeeCollectionAuthority(#companyId, authentication.name)")
				.antMatchers(HttpMethod.GET, "/cvbank/employer/company/{companyId}")
					.access("hasRole('EMPLOYER') or hasRole('ADMINISTRATOR')")
				.anyRequest()
					.authenticated();
		}
	}
	
//	@Configuration
//	@Order(1)     
//	public static class EmployeeConfigurerAdapter extends WebSecurityConfigurerAdapter {
//		@Override
//		public void configure(WebSecurity web) {
//			web.ignoring()
//			.antMatchers(HttpMethod.POST, "/cvbank/employee/register**");
//		}
//		
//		@Override
//		protected void configure(HttpSecurity http) throws Exception {
//			http.httpBasic();
//			http.csrf().disable();
//			http.sessionManagement()
//				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//			http.authorizeRequests()
//				.antMatchers("/cvbank/employee/login**")
//					.authenticated()
//				.antMatchers("/cvbank/employee/{id}**")
//					.access("#id == authentication.name")
//				.anyRequest()
//					.authenticated();
//		}
//	}
//	
//	@Configuration
//	@Order(2)
//	public static class EmployerConfigurerAdapter extends WebSecurityConfigurerAdapter {
//		@Override
//		public void configure(WebSecurity web) {
//			web.ignoring()
//			.antMatchers(HttpMethod.POST, "/cvbank/employer/register**");
//		}
//		
//		@Override
//		protected void configure(HttpSecurity http) throws Exception {
//			http.httpBasic();
//			http.csrf().disable();
//			http.sessionManagement()
//				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//			http.authorizeRequests()
//				.antMatchers("/cvbank/employer/login**")
//					.authenticated()
//				.antMatchers("/cvbank/employer/{id}**")
//					.access("#id == authentication.name")
//				.antMatchers("/cvbank/employer/{companyId}/collection**")
//					.access("@customSecurity.checkEmployeeCollectionAuthority(#companyId, authentication.name)")
//				.antMatchers("/cvbank/company/{companyId}**")
//					.access("hasRole('EMPLOYER') or hasRole('ADMINISTRATOR')")
//				.anyRequest()
//					.authenticated();
//		}
//	}
}
