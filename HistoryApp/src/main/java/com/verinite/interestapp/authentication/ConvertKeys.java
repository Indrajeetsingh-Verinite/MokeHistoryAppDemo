package com.verinite.interestapp.authentication;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.springframework.stereotype.Component;

@Component
public class ConvertKeys {

   public  PublicKey stringToPublicKey(String publicKey) throws InvalidKeySpecException, NoSuchAlgorithmException{


    KeyFactory keyFactory = KeyFactory.getInstance("EC");
		byte[] publicKeyEncoded = Base64.getDecoder().decode(publicKey);

		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKeyEncoded);
		return  keyFactory.generatePublic(x509EncodedKeySpec);   
    }

    public PrivateKey stringToPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException{
      KeyFactory keyFactory = KeyFactory.getInstance("EC");
	        byte[] privatekeyEncoded = java.util.Base64.getDecoder().decode(privateKey);

	        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privatekeyEncoded);
	        return keyFactory.generatePrivate(pkcs8KeySpec);
    }
    
}
