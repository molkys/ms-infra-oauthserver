package com.example.infra.oauth.controller;

import java.io.ByteArrayInputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.infra.oauth.Const;

@RestController
@RequestMapping(
        value = {"/admin"},
        produces = MediaType.APPLICATION_JSON_VALUE
)
@Validated
public class AdminController {

	private final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);
	
	private final JdbcTemplate jdbc;

	@Autowired
    private TokenStore tokenStore;
	
    public AdminController(DataSource dataSource) {
        this.jdbc = new JdbcTemplate(dataSource);
    }
	    
    @RequestMapping(method = RequestMethod.GET, path = "/token/list")
    @ResponseStatus(HttpStatus.OK)
    @Secured({"ROLE_ADMIN"})
    public List<String> findAllTokens() {
        
    	final Collection<OAuth2AccessToken> tokensByClientId = tokenStore.findTokensByClientId(Const.CLIENT_ID);
    	LOGGER.info("tokensByClientId: {}",tokensByClientId);
        return tokensByClientId.stream().map(item->item.getValue()).collect(Collectors.toList());
    	
    	//final List<byte[]> tokens = jdbc.queryForList("select token from oauth_access_token", byte[].class);
    	//LOGGER.info("tokens: {}",tokens);        
        //return tokens.stream().map(this::deserializeToken).collect(Collectors.toList());
    }
    
    private String deserializeToken(byte[] tokenBytes) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(tokenBytes);
            ObjectInput in = new ObjectInputStream(bis);
            DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) in.readObject();
            return token.getValue();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
