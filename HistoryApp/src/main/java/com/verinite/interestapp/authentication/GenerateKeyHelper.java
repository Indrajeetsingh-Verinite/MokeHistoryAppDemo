package com.verinite.interestapp.authentication;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class GenerateKeyHelper {
	
	 //gneratng key pair of public and private key
	  public static KeyPair generateKeyPair() {
	    return generateKeyPair("EC");
	  }

	  public static KeyPair generateKeyPair(String algorithm) {
	    try {
	      KeyPairGenerator ecKeyPairGenerator = KeyPairGenerator.getInstance(algorithm);
	      return ecKeyPairGenerator.generateKeyPair();
	    } catch (NoSuchAlgorithmException e) {
	      throw new RuntimeException("could not generate key-pair", e);
	    }
	  }

}
