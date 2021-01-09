package com.jmscott.rest.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jmscott.rest.auth.CustomUserDetailsService;
import com.jmscott.rest.auth.JwtTokenUtil;
import com.jmscott.rest.auth.resource.AuthenticationException;
import com.jmscott.rest.auth.resource.JwtTokenRequest;
import com.jmscott.rest.auth.resource.JwtTokenResponse;
import com.jmscott.rest.model.Password;
import com.jmscott.rest.model.User;
import com.jmscott.rest.model.UserWithPassword;
import com.jmscott.rest.repository.UserRepository;
import com.jmscott.rest.service.InstanceInfoService;

import io.jsonwebtoken.ExpiredJwtException;

@RestController
//@RequestMapping(path = "/security")
@CrossOrigin(origins= {"http://localhost:3001", "http://localhost:3000", "http://api.jmscottnovels.com:3001", "http://api.jmscottnovels.com:3000"}, allowCredentials = "true")
public class JwtAuthenticationRestController {

  @Value("${jwt.http.request.header}")
  private String tokenHeader;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtTokenUtil jwtTokenUtil;
  
  @Autowired
  private CustomUserDetailsService customUserService;

  @Autowired
  private UserRepository userRepository;
  
  @Autowired
  private InstanceInfoService instanceInfoService;
     
  @GetMapping
  public ResponseEntity<?> healthCheck() {
	  return ResponseEntity.ok("{healthy: true, instanceInfo: " + instanceInfoService.retrieveInstanceInfo() + "}");
  }
  
  @PostMapping(value = "${jwt.get.token.uri}")
  public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtTokenRequest authenticationRequest, HttpServletResponse response)
      throws AuthenticationException {

    authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

    final UserDetails userDetails = customUserService.loadUserByUsername(authenticationRequest.getUsername());
    
    final String token = jwtTokenUtil.generateToken(userDetails);
    final Date tokenDate = jwtTokenUtil.getExpirationDateFromToken(token);
    final Cookie sessionCookie = new Cookie("authenticationToken", token);
    sessionCookie.setHttpOnly(true);
    sessionCookie.setMaxAge(7 * 24 * 60 * 60);
    // TODO: enable once https is established
    //sessionCookie.setSecure(true);
    response.addCookie(sessionCookie);
    
    return ResponseEntity.ok(new JwtTokenResponse(token, tokenDate));
  }
  
  @PostMapping(value = "/signup")
  public ResponseEntity<?> createUser(@RequestBody UserWithPassword user) throws IOException {
	  User newUser = new User(user.getUsername(), user.getPerson(), true);
	  User savedUser = userRepository.save(newUser);
	  Password password = new Password(savedUser.getId(), user.getPassword());
	  customUserService.saveUser(savedUser, password);
	  
	  return ResponseEntity.ok().build();
  }
  
  // TODO: test
  @PostMapping(value = "/reset/{id}")
  public ResponseEntity<?> passwordResetEmail(@PathVariable String id, @RequestBody String email) throws IOException, MessagingException {
	  Properties prop = new Properties();	  
	  prop.put("mail.smtp.auth", true);
	  prop.put("mail.smtp.starttls.enable", "true");
	  prop.put("mail.smtp.host", "smtp.mailtrap.io");
	  prop.put("mail.smtp.port", "25");
	  prop.put("mail.smtp.ssl.trust", "smtp.mailtrap.io");
	  
	  Session session = Session.getInstance(prop, new Authenticator() {
		  protected PasswordAuthentication getPasswordAuthentication() {
			  return new PasswordAuthentication("userName", "password");
		  }
	  });
	  
	  Message message = new MimeMessage(session);
	  message.setFrom(new InternetAddress(email));
	  message.setRecipients(
	    Message.RecipientType.TO, InternetAddress.parse("admin@jmscottnovels.com"));
	  message.setSubject("reset for UBlog");

	  String msg = "Click <a href=\"jmscottnovels.com/" + id + "\">here</a> to reset your UBlog password";

	  MimeBodyPart mimeBodyPart = new MimeBodyPart();
	  mimeBodyPart.setContent(msg, "text/html");

	  Multipart multipart = new MimeMultipart();
	  multipart.addBodyPart(mimeBodyPart);

	  message.setContent(multipart);

	  Transport.send(message);
	  
	  return ResponseEntity.ok().build();
  }
  
//  @PostMapping(value = "/signup")
//  public ResponseEntity<?> createUser(@RequestBody User user) {
//	  User userExists = customUserService.findUserByUsername(user.getUsername());
//	  if(userExists != null) {
//		  return ResponseEntity.status(HttpStatus.CONFLICT).header("Conflicting-User", user.getUsername()).build();
//	  } else {
//		  customUserService.saveUser(user);
//		  return ResponseEntity.ok().build();
//	  }
//  }

  //@GetMapping(value = "${jwt.refresh.token.uri}")
  //public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request, HttpServletResponse response) {
//  if (jwtTokenUtil.canTokenBeRefreshed(jwtToken)) {
//	  String refreshedToken = jwtTokenUtil.refreshToken(jwtToken);
//	  Date tokenDate = jwtTokenUtil.getExpirationDateFromToken(refreshedToken);
//	  sessionCookie = new Cookie("authenticationToken", refreshedToken);	// replace cookie with refreshed token cookie
//	  sessionCookie.setHttpOnly(true);
//	  sessionCookie.setMaxAge(7 * 24 * 60 * 60);
//	  // TODO: enable once https is established
//	  //sessionCookie.setSecure(true);
//	  response.addCookie(sessionCookie);
//
//	  return ResponseEntity.ok(new JwtTokenResponse(token, tokenDate));
//  } else {
//	  return ResponseEntity.badRequest().body(null);
//  }
  @GetMapping(value = "/validate")
  public ResponseEntity<?> validateAuthenticationToken(HttpServletRequest request, HttpServletResponse response) {
	  final Cookie[] allCookies = request.getCookies();
	  Cookie sessionCookie = null;
	  String jwtToken = null;
	  String username = null;
	  
	  if(allCookies != null) {
		  sessionCookie = Arrays.stream(allCookies).filter(c -> c.getName().equals("authenticationToken")).findFirst().orElse(null);
		  if(sessionCookie != null) {
			  jwtToken = sessionCookie.getValue();
			  try {
                  username = jwtTokenUtil.getUsernameFromToken(jwtToken);
              } catch (IllegalArgumentException e) {
            	  return ResponseEntity.status(HttpStatus.FORBIDDEN).header("AccessDenied", "Inactive user").build();
              } catch (ExpiredJwtException e) {
            	  return ResponseEntity.status(HttpStatus.FORBIDDEN).header("AccessDenied", "Token expired").build();
              }
		  } else {
			  return ResponseEntity.status(HttpStatus.FORBIDDEN).header("AccessDenied", "No active authentication session").build();
		  }
	  } else {
		  return ResponseEntity.status(HttpStatus.FORBIDDEN).header("AccessDenied", "No active authentication session").build();
	  }

	  return ResponseEntity.ok(username);
  }

  @ExceptionHandler({ AuthenticationException.class })
  public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
  }

  private void authenticate(String username, String password) {
    Objects.requireNonNull(username);
    Objects.requireNonNull(password);

    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    } catch (DisabledException e) {
      throw new AuthenticationException("USER_DISABLED", e);
    } catch (BadCredentialsException e) {
      throw new AuthenticationException("INVALID_CREDENTIALS", e);
    }
  }
  
}