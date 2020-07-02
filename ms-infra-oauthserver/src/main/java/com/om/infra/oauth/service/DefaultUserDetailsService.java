package com.om.infra.oauth.service;

import java.util.Collections;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.om.infra.oauth.model.AppUser;
import com.om.infra.oauth.repository.IAppUserRepository;

/**
 * In-Memory implementation for security passing only user name
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
public class DefaultUserDetailsService implements UserDetailsService {

	private final IAppUserRepository appUserRepository;

	public DefaultUserDetailsService(IAppUserRepository appUserRepository) {
		this.appUserRepository=appUserRepository;
	}
	
	@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		final Optional<AppUser> userEntity = appUserRepository.findById(username);

        if (userEntity.isPresent()) {
            final AppUser appUser = userEntity.get();

            return new User(appUser.getUserEmail(),
                    passwordNoEncoding(appUser),
                    Collections.singletonList(new SimpleGrantedAuthority(appUser.getUserRole())));
        }

        return null;
    }

	private String passwordNoEncoding(AppUser appUser) {
        // you can use one of bcrypt/noop/pbkdf2/scrypt/sha256
        // more: https://spring.io/blog/2017/11/01/spring-security-5-0-0-rc1-released#password-encoding
        return "{noop}" + appUser.getUserPass();
    }
	    
}
