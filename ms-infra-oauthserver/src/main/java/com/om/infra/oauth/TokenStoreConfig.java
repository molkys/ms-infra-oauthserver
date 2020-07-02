package com.om.infra.oauth;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

@Configuration
public class TokenStoreConfig {

	 @Autowired
	    private DataSource dataSource;

	 /**
	  * Token persistence
	  * @return
	  */
	    @Bean
	    public TokenStore tokenStore() {
	        return new JdbcTokenStore(dataSource);
	    }

	    /**
	     * Ued to manipulate a token
	     * @return DefaultTokenServices
	     */
	    @Bean
	    public DefaultTokenServices tokenServices() {
	        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
	        defaultTokenServices.setTokenStore(tokenStore());
	        defaultTokenServices.setSupportRefreshToken(true);
	        return defaultTokenServices;
	    }
	    
}
