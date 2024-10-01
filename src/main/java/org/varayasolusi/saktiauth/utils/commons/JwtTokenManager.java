package org.varayasolusi.saktiauth.utils.commons;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/*
 * https://www.viralpatel.net/java-create-validate-jwt-token/
 */

@Component
public class JwtTokenManager {

	private String secret = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";
	
	private Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret), 
            SignatureAlgorithm.HS256.getJcaName());
	
	public String createJwtToken(String userId, String id) { 


		Instant now = Instant.now();
		String jwtToken = Jwts.builder()
		        .claim("userId", userId)
		        .setSubject(userId)
		        .setId(id)
		        .setIssuedAt(Date.from(now))
		        .setExpiration(Date.from(now.plus(5l, ChronoUnit.MINUTES)))
		        .signWith(hmacKey)
		        .compact();
		
		return jwtToken;
	}
	
	public Jws<Claims> parseJwt(String jwtString) {

	    Jws<Claims> jwt = Jwts.parserBuilder()
	            .setSigningKey(hmacKey)
	            .build()
	            .parseClaimsJws(jwtString);

	    return jwt;
	}
}