package com.naveen.advice;

import java.nio.file.AccessDeniedException;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;

@RestControllerAdvice // it will work with DispatchServlet, If dispatchServlet will get request then @RestControllerAdvice will work
public class CustomExceptionHandler {

	ProblemDetail errorDetail = null;
	
	@ExceptionHandler(Exception.class)
	public ProblemDetail handleSecurityException(Exception ex) {

		if (ex instanceof BadCredentialsException) {
			errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401), ex.getMessage());
			errorDetail.setProperty("access_denied_reason", "Authentication Failure");

		}

		if (ex instanceof AccessDeniedException) {
			errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), ex.getMessage());
			errorDetail.setProperty("access_denied_reason", "Not_Authorized!");

		}
		
		// this below 2 type of exception is not handled by DispatchServlet

		if (ex instanceof SignatureException) {
			errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), ex.getMessage());
			errorDetail.setProperty("access_denied_reason", "JWT Signature not valid");

		}

		if (ex instanceof ExpiredJwtException) {
			errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), ex.getMessage());
			errorDetail.setProperty("access_denied_reason", "JWT token already expired !");

		}

		return errorDetail;

	}
	
	
}
