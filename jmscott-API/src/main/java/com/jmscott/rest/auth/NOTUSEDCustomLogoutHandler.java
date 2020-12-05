package com.jmscott.rest.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

public class NOTUSEDCustomLogoutHandler implements LogoutHandler{
	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		System.out.println(request.toString());
		response.addHeader("Access-Control-Allow-Origin", "http://locahost:3000");
		//response.addHeader("Access-Control-Allow-Credentials", "true");
	    // logs?
	}
}
