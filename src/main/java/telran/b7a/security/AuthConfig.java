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
                    .antMatchers(HttpMethod.POST, "/cvbank/employee/signup/**", "/cvbank/employer/signup/**", "/cvbank/admin/signup/**")
                    .antMatchers(HttpMethod.GET, "/cvbank/notify/**");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.cors().and().httpBasic().and().csrf().disable();
            http.sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            http.authorizeRequests()
                    //==================CV=================
                    .antMatchers(HttpMethod.POST, "/cvbank/cv")                //Add CV
                    .access("hasRole('EMPLOYEE')")
                    .antMatchers(HttpMethod.DELETE, "/cvbank/cv/{cvid}")    //Delete CV
                    .access("@customSecurity.checkCVAuthority(#cvid, authentication.name)")
                    .antMatchers(HttpMethod.GET, "/cvbank/cv/{cvid}")        //Get CV
                    .access("hasRole('ADMINISTRATOR') or hasRole('EMPLOYER') or @customSecurity.checkCVAuthority(#  cvid, authentication.name)")
                    .antMatchers(HttpMethod.PUT, "/cvbank/cv/{cvid}")        //Update CV
                    .access("@customSecurity.checkCVAuthority(#cvid, authentication.name)")
                    .antMatchers("/cvbank/cv/anonymizer/{cvid}")            //Anonymize CV
                    .access("@customSecurity.checkCVAuthority(#cvid, authentication.name)")
                    .antMatchers("/cvbank/cv/cvs/aggregate")                //Aggregate CVs
                    .access("hasRole('EMPLOYER')")
                    //==================EMPLOYEE=================
                    .antMatchers(HttpMethod.POST, "/cvbank/employee/signin")        //Login Employee
                    .authenticated()
                    .antMatchers(HttpMethod.PUT, "/cvbank/employee/login")            //Change Employee Login
                    .authenticated()
                    .antMatchers(HttpMethod.PUT, "/cvbank/employee/pass")            //Change Employee Password
                    .authenticated()
                    .antMatchers("/cvbank/employee/{id}")                            //Update Employee | Delete Employee | Get Employee
                    .access("#id == authentication.name")
                    //==================EMPLOYER=================
                    .antMatchers("/cvbank/employer/signin")                            //Login Employer
                    .permitAll()
                    .antMatchers("/cvbank/employer/login")                            //Change login
                    .authenticated()
                    .antMatchers("/cvbank/employer/pass")                            //Change password
                    .authenticated()
                    .antMatchers("/cvbank/employer/{companyId}")                    //Update Employer | Delete Employer
                    .access("#companyId == authentication.name")
                    .antMatchers(HttpMethod.PUT, "/cvbank/employer/{companyId}/collection/{collectionName}")        //Add CV Collection
                    .access("#companyId == authentication.name")
                    .antMatchers(HttpMethod.PUT, "/cvbank/employer/{companyId}/collection/{collectionName}/{cvId}")    //Add CV to collection
                    .access("#companyId == authentication.name")
                    .antMatchers(HttpMethod.GET, "/cvbank/employer/company/{companyName}")            //Find Employer by company name
                    .authenticated()
                    //==================ADMIN=================
                    .antMatchers("/cvbank/admin/signin")                                //Login Admin
                    .permitAll()
                    .antMatchers("/cvbank/admin/expert")                                //Add expert | Update expert
                    .access("hasRole('ADMINISTRATOR')")
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
//			.antMatchers(HttpMethod.POST, "/cvbank/employee/signup**");
//		}
//		
//		@Override
//		protected void configure(HttpSecurity http) throws Exception {
//			http.httpBasic();
//			http.csrf().disable();
//			http.sessionManagement()
//				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//			http.authorizeRequests()
//				.antMatchers(HttpMethod.POST, "/cvbank/employee/signin")		//Login Employee
//					.authenticated()
//				.antMatchers(HttpMethod.PUT, "/cvbank/employee/login")			//Change Employee Login
//					.authenticated()
//				.antMatchers(HttpMethod.PUT, "/cvbank/employee/pass")			//Change Employee Password
//					.authenticated()
//				.antMatchers("/cvbank/employee/{id}")							//Update Employee | Delete Employee | Get Employee
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
//			.antMatchers(HttpMethod.POST, "/cvbank/employer/signup**");
//		}
//		
//		@Override
//		protected void configure(HttpSecurity http) throws Exception {
//			http.httpBasic();
//			http.csrf().disable();
//			http.sessionManagement()
//				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//			http.authorizeRequests()
//			.antMatchers("/cvbank/employer/signin")							//Login Employer
//				.authenticated()
//			.antMatchers("/cvbank/employer/login")							//Change login
//				.authenticated()
//			.antMatchers("/cvbank/employer/pass")							//Change password
//				.authenticated()
//			.antMatchers("/cvbank/employer/{companyId}")					//Update Employer | Delete Employer
//				.access("#companyId == authentication.name")
//			.antMatchers(HttpMethod.PUT, "/cvbank/employer/{companyId}/collection/{collectionName}")		//Add CV Collection
//				.access("#companyId == authentication.name")
//			.antMatchers(HttpMethod.PUT, "/cvbank/employer/{companyId}/collection/{collectionName}/{cvId}")	//Add CV to collection
//				.access("#companyId == authentication.name")
//			.antMatchers(HttpMethod.GET, "/cvbank/employer/company/{companyName}")			//Find Employer by company name
//				.authenticated()
//			.anyRequest()
//				.authenticated();
//		}
//	}
}
