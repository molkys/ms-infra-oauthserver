package com.example.infra.oauth.service;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * In-Memory implementation for security passing user name and password
 * 
 * TESTING on postman.
 * 
 * 1) POST request for getting authentication token:
 * http://my-client:my-secret@localhost:8080/oauth/token?username=test@test.com&password=tester&grant_type=password
 * 
 * 2) GET request for invoking API
 * http://localhost:8080/api/hello?name=Amilcar&access_token={access_token_FROM_STEP_1}
 * @author orodr
 *
 */
@Service
public class DefaultAuthenticationProvider implements AuthenticationProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAuthenticationProvider.class);
	
    private static final String VALID_USER_EMAIL = "test@test.com";
    private static final String VALID_USER_PASSWORD = "tester";
    
    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
       
    	final String userEmail = authentication.getName();
        final Object userPassword = authentication.getCredentials();

        if (userEmail == null || userPassword == null) {
        	
    		LOGGER.error("Null user name or password");
            return null;
        }

        if (userEmail.isEmpty() || userPassword.toString().isEmpty()) {
        	
        	LOGGER.error("Empty user name or password");
            return null;
        }

        if (userEmail.equalsIgnoreCase(VALID_USER_EMAIL)
                && userPassword.equals(VALID_USER_PASSWORD)) {
        	
        	LOGGER.error("Valid user name and password");
        	
            return new UsernamePasswordAuthenticationToken(
                    userEmail, userPassword, getAuthority());
        }

        throw new UsernameNotFoundException("Invalid username or password.");
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private List<SimpleGrantedAuthority> getAuthority() {
        return Collections.emptyList();
    }

}
