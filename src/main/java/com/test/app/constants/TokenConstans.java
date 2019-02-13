package com.test.app.constants;

import java.security.Key;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class TokenConstans {

	public static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
	
	public static final Long EXPIRATION_DATE = 14000000L;
	
	public static final String PREFIX_TOKEN = "Bearer ";
	
	public static final String HEADER_STRING = "Authoritation";
	
	public static final String ROLE_TOKEN = "authorities";

}
