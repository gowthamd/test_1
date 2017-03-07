package com.trippal.auth;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;

/**
 * JWT authentication helper
 *
 */

public class AuthHelper {
	final long ONE_MINUTE_IN_MILLIS = 60000;

	public String createJWTToken(String username) throws Exception {
		Date issuedAt = new Date();
		Date expiresAt = new Date(issuedAt.getTime() + (5 * ONE_MINUTE_IN_MILLIS));
		String token = JWT.create().withIssuer("tippal").withIssuedAt(issuedAt)
				.withExpiresAt(expiresAt).sign(Algorithm.HMAC256("secret"));
		return token;
	}

	public void verifyToken(String token) throws Exception {
		JWTVerifier verifier = JWT.require(Algorithm.HMAC256("secret")).withIssuer("tippal").build();
		verifier.verify(token);
	}

}
