package com.om.infra.oauth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
        value = {"/oauth"},
        produces = MediaType.APPLICATION_JSON_VALUE
)
@Validated
public class TokenController {

	@Autowired
    private DefaultTokenServices tokenServices;

	/**
	 * Injecting Authentication will make this method only available for the users that already have a valid token
	 * @param authentication Authentication
	 */
    @RequestMapping(method = RequestMethod.DELETE, path = "/revoke")
    @ResponseStatus(HttpStatus.OK)
    public void revokeToken(Authentication authentication) {
        final String userToken = ((OAuth2AuthenticationDetails) authentication.getDetails()).getTokenValue();
        tokenServices.revokeToken(userToken);
    }
    
}
