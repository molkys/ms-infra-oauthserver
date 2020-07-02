package com.example.infra.oauth.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.infra.oauth.model.AppUser;
import com.example.infra.oauth.repository.IAppUserRepository;

/**
 * Database implementation for security passing user name and password
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
public class DatabaseAuthenticationProvider implements AuthenticationProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseAuthenticationProvider.class);
	
	private final IAppUserRepository appUserRepository;

    public DatabaseAuthenticationProvider(IAppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

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

        final Optional<AppUser> appUser = this.appUserRepository.findById(authentication.getName());

        if (appUser.isPresent()) {
            final AppUser user = appUser.get();
            final String providedUserEmail = authentication.getName();
            final Object providedUserPassword = authentication.getCredentials();

            if (providedUserEmail.equalsIgnoreCase(user.getUserEmail())
                    && providedUserPassword.equals(user.getUserPass())) {
            	
            	LOGGER.info("Valid user name and password");
            	
                return new UsernamePasswordAuthenticationToken(
                        user.getUserEmail(),
                        user.getUserPass(),
                        Collections.singleton(new SimpleGrantedAuthority(user.getUserRole())));
            }
        }

        throw new UsernameNotFoundException("Invalid username or password.");
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
