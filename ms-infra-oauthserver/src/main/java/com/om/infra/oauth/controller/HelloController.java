package com.om.infra.oauth.controller;

import org.springframework.boot.actuate.trace.http.HttpTrace.Principal;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.om.infra.oauth.model.Welcome;

@RestController
@RequestMapping(
        value = {"/principal"},
        produces = MediaType.APPLICATION_JSON_VALUE
)
// indicating that a specific class is supposed to be validated at 
// the method level (acting as a pointcut for the corresponding validation 
// interceptor)
@Validated
public class HelloController {

	/**
	 * Using DefaultUserDetailsService
	 * @param name user name
	 * @return String
	 */
	@GetMapping("/greetings")
    @ResponseStatus(HttpStatus.OK)
    public Welcome greetings(@RequestParam("name") String name) {
        return new Welcome(name);
    }
	
	/**
	 * Using DefaultAuthenticationProvider
	 * @param name user name
	 * @return String 
	 */
	@GetMapping("/greetings/user")
    @ResponseStatus(HttpStatus.OK)
    public Welcome greetingsUser(@RequestParam("name") String name, Principal principal) {
        return new Welcome(name + " (" + principal.getName() + ")");
    }
	
	@GetMapping
    public ResponseEntity<Principal> get(final Principal principal) {
        return ResponseEntity.ok(principal);
    }
}
