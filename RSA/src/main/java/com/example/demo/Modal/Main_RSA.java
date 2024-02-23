package com.example.demo.Modal;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import org.springframework.stereotype.Component;

@Component
public class Main_RSA {
    
    public KeyPair gen() throws Exception {
        KeyPairGenerator kg = null;
        kg = KeyPairGenerator.getInstance("RSA");
        kg.initialize(2048);
        return kg.generateKeyPair();        
    }
    
    public  String privateKeyToString(PrivateKey privateKey) {
        byte[] privateKeyBytes = privateKey.getEncoded();
        return Base64.getEncoder().encodeToString(privateKeyBytes);
    }
    
    private static PrivateKey stringToPrivateKey(String privateKeyString) throws GeneralSecurityException {
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyString);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        return keyFactory.generatePrivate(keySpec);
    }
    public static String decrypt_message(byte arr[],String pri_str) throws GeneralSecurityException
    {
        PrivateKey pri_key= stringToPrivateKey(pri_str);  
        Cipher cipher =null ;
        cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, pri_key);        
        byte arr1[]=cipher.doFinal(arr);        
        return new String(arr1);
    }   
    public  String encrypt_message(String message,PublicKey pub_key,PrivateKey pri_key) throws GeneralSecurityException
    {
        Cipher cipher = null;
        cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pub_key);        
        byte arr[]=cipher.doFinal(message.getBytes()); 
        return Base64.getEncoder().encodeToString(arr);       
    }   

 
}

