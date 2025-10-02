package com.naveen.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {

	public static final String SECRET = "357638792F423F4428472B4B6250655368566D597133743677397A2443264629";
	
	private Key getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
//	<---------- Depricate ----------->
//	private String createToken(Map<String, Object> claims, String username) {
//		
//		return Jwts.builder()
//				.setClaims(claims)
//				.setSubject(username)
//				.setIssuedAt(new Date(System.currentTimeMillis()))
//				.setExpiration(new Date(System.currentTimeMillis() + 1000*60*5))
//				.signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
//	}
	
//	private 
	public String createToken(Map<String, Object> claims, String username) {
	    return Jwts.builder()
	            .claims(claims) // new style, instead of setClaims
	            .subject(username) // instead of setSubject
	            .issuedAt(new Date(System.currentTimeMillis()))
	            .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 5))	// 5 min
	            .signWith(getSignKey(), SignatureAlgorithm.HS384)  // no need to pass algo ( SignatureAlgorithm.HS256)
	            .compact();
	}
	
	public String generateRefreshToken(String username) {
	    return Jwts.builder()
	            .subject(username)
	            .issuedAt(new Date(System.currentTimeMillis()))
	            .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7)) // 7 days
	            .signWith(getSignKey(), SignatureAlgorithm.HS384)
	            .compact();
	}
	
	public String generateToken(String username) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, username);
	}
	
//	private
	public Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
	}
	
//	private Claims extractAllClaims(String token) {
//	    return Jwts.parser()
//	            .verifyWith(getSignKey())  // ✅ correct for 0.12.x+
//	            .build()
//	            .parseSignedClaims(token)
//	            .getPayload();
//	}

	
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
		
		final Claims claims = extractAllClaims(token);
		
		return claimsResolver.apply(claims);
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

//	private 
	public Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

}
