package com.verinite.interestapp.authentication;

import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.verinite.interestapp.exception.ErrorCodes;
import com.verinite.interestapp.exception.InterestAppException;

import java.security.PublicKey;
import java.security.interfaces.ECPublicKey;

import org.springframework.stereotype.Service;

@Service
public class JWTHelper {

	private static final String JWT_ERROR = "JWT Not Valid";


	public static final String CHALLENGE_INPUT = "challengeInput";

	public String verifyJWTClaimsAndReturnChallange(String token, PublicKey publickey) {

		try {
			SignedJWT jwt = SignedJWT.parse(token);
			boolean res = jwt.verify(new ECDSAVerifier((ECPublicKey) publickey));
			if (!res)
				throw new InterestAppException(ErrorCodes.UNAUTHORIZED, JWT_ERROR);
			JWTClaimsSet claimsSet = jwt.getJWTClaimsSet();
			if (System.currentTimeMillis() > claimsSet.getExpirationTime().toInstant().toEpochMilli()) {
				throw new InterestAppException(ErrorCodes.ENROLLMENT_FAILED_DUE_TO_PRECONDITION, "Expired");
			}
			return claimsSet.getStringClaim(CHALLENGE_INPUT);
		} catch (Exception e) {
			throw new InterestAppException(ErrorCodes.UNAUTHORIZED, JWT_ERROR);
		}
	}
}