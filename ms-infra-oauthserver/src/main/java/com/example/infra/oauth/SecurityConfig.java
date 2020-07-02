package com.example.infra.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.infra.oauth.service.DatabaseAuthenticationProvider;
import com.example.infra.oauth.service.DefaultAuthenticationProvider;

/**
 * Security provider configuration
 * @author orodr
 *
 */
@Configuration
@EnableWebSecurity
//securedEnabled = true => enables authorization
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	/**
	 * Reusable service to validate user's credentials in-memory stored (only user name)
	 */
	@Autowired
    private UserDetailsService userDetailsService;

	/**
	 * Reusable service to validate user's credentials in-memory stored (user name and password)
	 */
	@Autowired
    private DefaultAuthenticationProvider defaultAuthenticationProvider;
	
	/**
	 * Reusable service to validate user's credentials in-database stored (user name and password)
	 */
	@Autowired
    private DatabaseAuthenticationProvider databaseAuthenticationProvider;
	
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        
    	//auth.userDetailsService(userDetailsService);
    	//auth.authenticationProvider(defaultAuthenticationProvider);
    	auth.authenticationProvider(databaseAuthenticationProvider);
    }
}
