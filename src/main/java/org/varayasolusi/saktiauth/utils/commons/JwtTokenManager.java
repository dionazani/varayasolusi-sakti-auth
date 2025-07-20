package org.varayasolusi.saktiauth.utils.commons;

import java.security.Key;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;

/*
 * https://www.viralpatel.net/java-create-validate-jwt-token/
 */

@Component
public class JwtTokenManager {

	@Value("${jwt.secret}")
	private String secret;

	private Key hmacKey;

	@PostConstruct
	public void init() {
		this.hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret), 
				SignatureAlgorithm.HS256.getJcaName());
	}

	public String createJwtToken(String appUserId, String id) { 

		Timestamp originalTimestamp = new Timestamp(System.currentTimeMillis());
		Instant instant = originalTimestamp.toInstant().plus(Duration.ofMinutes(60));
		Timestamp newTimestamp = Timestamp.from(instant);
		
		System.out.println("originalTimestamp :" + originalTimestamp);
		System.out.println("newTimestamp " + newTimestamp);
		
		String jwtToken = Jwts.builder()
				.setIssuer(appUserId)
				.setId(id)
				.setSubject("TOKEN")
				.setIssuedAt(originalTimestamp)
				.setExpiration(newTimestamp)
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

	private <T> T extractClaim(String token, Function<Claims, T> claimsRevolver) {
		final Claims claims = parseJwt(token).getBody();
		return claimsRevolver.apply(claims);

	}

    //for retrieveing any information from token we will need the secret key
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}
	
	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}
	
	//retrieve expiration date from jwt token
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}
		
	public Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}
}