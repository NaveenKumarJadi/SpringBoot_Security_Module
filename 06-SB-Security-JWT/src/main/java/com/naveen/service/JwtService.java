package com.naveen.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/*
JWT is a compact, URL-safe token format used to represent claims securely between parties. It consists of three parts:

Header: Contains metadata about the token (e.g., algorithm type).
Payload: Contains claims (user data).
Signature: Ensures the token hasn't been tampered with.
*/

@Component
public class JwtService {

    // Replace this with a secure key in a real application, ideally fetched from environment variables
    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    // Generate token with given user name
    public String generateToken(String userName) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userName);
    }

    // Create a JWT token with specified claims and subject (user name)
    private String createToken(Map<String, Object> claims, String userName) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName) // The sub (subject) claim stores the username.
                .setIssuedAt(new Date()) // Records the token's creation time.
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 1)) // Sets the token’s expiration time (current time + 30 minutes).
                .signWith(getSignKey(), SignatureAlgorithm.HS256) //  Signs the token using the HS256 algorithm and a secret key.
                .compact(); // Converts the JWT object into a compact, URL-safe string.
    }
    // SignatureAlgorithm.HS256: is a symmetric algorithm where the same key is used for both signing and verification.
    
    // Get the signing key for JWT token
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Extract the username from the token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract the expiration date from the token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // A generic method to extract any claim from the token.
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims); // Uses a function to extract the specific claim.
    }

    // Extract all claims from the token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey()) // Verifies the token’s signature with the secret key.
                .build()
                .parseClaimsJws(token) // Parses the token and validates the signature.
                .getBody(); // Extracts the payload (claims)
    }
    
    // Checks if the token is expired by comparing the expiration date with the current date.
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    
    // Validates the token by: Checking if the token’s username matches the provided username. Ensuring the token hasn’t expired.
    // Validate the token against user details and expiration
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}