package com.verinite.interestapp.authentication;

import java.security.PrivateKey;
import java.security.interfaces.ECPrivateKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

public class GenerateToken {
	
	public String getToken(String challengeInp, int expirationInMinutes, PrivateKey privateKey) throws JOSEException {
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .jwtID(UUID.randomUUID().toString())
                .claim(JWTHelper.CHALLENGE_INPUT, challengeInp)
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(expirationInMinutes, ChronoUnit.MINUTES).toEpochMilli()))
                .build();
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.ES256);
        SignedJWT toSign = new SignedJWT(jwsHeader, jwtClaimsSet);
        toSign.sign(new ECDSASigner((ECPrivateKey) privateKey));
        return toSign.serialize();
    }

}
