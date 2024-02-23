package com.example.demo.Controller;
import java.util.*;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.RsaApplication;
import com.example.demo.Modal.Main_RSA;
import com.example.demo.Modal.User;
import com.example.demo.Repositary.UserRepositary;
import com.example.demo.Service.SendEmail;
import com.example.demo.Service.UserService;


@Controller
public class IndexController {
	

	@Autowired
	UserService userService;
	
	@Autowired
	User user;
	
	@Autowired
	SendEmail send;
	
	@Autowired
	Main_RSA m;
	
	@GetMapping("")
	public String index() {		
		return "index";
	}
	@GetMapping("/encrypt")
	public String encrypt() {
		return "encrypt";
	}
	
	@GetMapping("/savedata")	
	public String savedata(@RequestParam String semail,@RequestParam String remail,@RequestParam String msg) throws Exception {
		
		//generate private key and public key
		KeyPair keypair= m.gen();
	    PublicKey public_key=keypair.getPublic();
	    PrivateKey private_key=keypair.getPrivate();	
	    
	    //setting the user POJO class variable
	    user.setSender_email(semail);
	    String pkey=m.privateKeyToString(private_key);
	    //System.out.println(private_key);
	    String privateKeyStringArray[] = new String[7];
	    int index=0;
	    String temppkey="";
	    for(int i=0;i<pkey.length();i++) 
	    {
	    	if(temppkey.length()==254)
	    	{
	    		privateKeyStringArray[index++]=temppkey;
	    		temppkey="";
	    	}
	    	temppkey+=pkey.charAt(i)+""; 
	    }
	    if(temppkey!="")	    
	    	privateKeyStringArray[index++]=temppkey;
	    
//	    for(int i=0;i<7;i++)
//	    	System.out.println(i + ""+privateKeyStringArray[i]);
	    
	    user.setPrivate_key1(privateKeyStringArray[0]);
	    user.setPrivate_key2(privateKeyStringArray[1]);
	    user.setPrivate_key3(privateKeyStringArray[2]);
	    user.setPrivate_key4(privateKeyStringArray[3]);
	    user.setPrivate_key5(privateKeyStringArray[4]);
	    user.setPrivate_key6(privateKeyStringArray[5]);
	    user.setPrivate_key7(privateKeyStringArray[6]);
	    String e_msg_string = m.encrypt_message(msg, public_key, private_key);
	    
	    String encryptedStringArray[] = new String[2];
	    index=0;
	    temppkey="";
	    for(int i=0;i<e_msg_string.length();i++) 
	    {
	    	if(temppkey.length()==254)
	    	{
	    		encryptedStringArray[index++]=temppkey;
	    		temppkey="";
	    	}
	    	temppkey+=e_msg_string.charAt(i)+""; 
	    }
	    if(temppkey!="")	    
	    	encryptedStringArray[index++]=temppkey;
	    
	    
	    
	    
	    
	    user.setEncrypted_msg1(encryptedStringArray[0]);
	    user.setEncrypted_msg2(encryptedStringArray[1]);
	    //System.out.println("Private key :"+ user.getPrivate_key());
	    //saved into the database
	    userService.saveUser(user);
	    
	    //sending email
	    send.sendmail(remail, "Encrypted message's key", pkey);
		return "index";
	}
	
	@GetMapping("/decrypt")
	public String decrypt() {		
		return "decrypt";
	}
	
	@GetMapping("/decryptdata")
	@ResponseBody
	public String decryptdata(@RequestParam String semail,@RequestParam String pkey) throws GeneralSecurityException, UnsupportedEncodingException {	
		int index=0;
	    String temppkey="";
	    String privateKeyStringArray[] = new String[7];
	    for(int i=0;i<pkey.length();i++) 
	    {
	    	if(temppkey.length()==254)
	    	{
	    		privateKeyStringArray[index++]=temppkey;
	    		temppkey="";
	    	}
	    	temppkey+=pkey.charAt(i)+""; 
	    }
	    if(temppkey!="")	    
	    	privateKeyStringArray[index++]=temppkey;
	    
		ArrayList<User> l = new ArrayList<>();		
		l=userService.fetchByEmailAndPk(semail, privateKeyStringArray[0]);
		
		String estring1 = l.get(0).getEncrypted_msg1();
		String estring2 = l.get(0).getEncrypted_msg2();
		String estring = estring1+estring2;
		
//	    //String estring = ",+MkNPJm0BUnMVPXCGbdDN2tjtF1RRCMROwtDOgfK3CUCXSm04iJKsBqzXnSshrBG1xGGWAczKeG8RqUoGvIFfe2A9od9Km33dCGFCOQR8MWlb7SYWNYeJLyWfv7WLR6YdmuIcPWvY28Qy7dkMA/S6Sl1lKV/xUsYlivqmoI0t187MzZJCjNMrJnnvfNk/sRW6Em0QNYULy7pq2eNjyqxi5W2pkr8QujMWEhdyZ3PIFviZc8JHAC";
//	    
//	    String p_key1 = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQC1OZ4+9gJLUqIydqAvaXMgyTEnh8Xn6Ryno5NMZ7X+Q1ACvmwfPt+Fut440FjqaN/0fOOw5bqpxcmSCSpYclxpFYUOQd+oO2RtgdEb0Nh2TOOtc3lkjWOQr8PqdlIVEXWgXeTACLjLttia2fiDEECUZOE7HnDHZyl3ksS23Zhk6Wh4tQwIJXrMk0n9C4dEkhmFuzyX+/0L0cRo6phOZpVDEiQSa8aKc23bWxnNM/7vfhukDfEF9qCdBEQFTbpXYsKgC3rj2G1afC9RafRIfTpRrCunx46gyhIm9fz77117B7rtmxW1jbBScePivLkMVmVxyADrCsQiRf7f3SlCtxZzAgMBAAECggEAGVVsJz4JtRDgprBJlUcm9nLZob6Lrm86d77YpQoseVZclX2cLMQDyDSGwydw/pfZktpwk/aRYRFKq9UHK+UUwQXmm0cfJmvjDqHiowJPeD4oZ8NoOWPAguXnO9v9fn9504i8pRGzD5tIbWFCKcQ4WuY9TB/lwSJNPC9yu2N/3b88ZKrN+P2PeAwVlM3nf53SVvl6PgIxcng4jUoFva8ZGSU5KxBb7hys9DFLJAU95nb+kuhEHFUd3qPQPZthZdctyA129r50JUnLvumjgrXV/fMGKDOWBhf8Co6Dnsmre+t2LRmLt4QIbzt9hQJdIJMOFXa4FPMby8xqXzTzh5jlYQKBgQC6Mifb4thoQgpn3AaylqGJsJCm/5h2f0yfFuZhFyT46+VNUPe2TPWgJnZzECHggCrB0zGfDvSlZ0zJA4+3/QyHpZLwy05bjDRktc11fq7xO7EmMw6dmyMPj9G1ouVzzipgWJXwzLBjPHwmtv6o6s9xWNjuFVtTN0Vz/m5jwPbgIQKBgQD5KmS1bPlXoJSLwp0gMeREpMnBd1kTfSy4MEmDd3InYNILEiqA69bFtGIewlA5dsZ9LjVxpkYkhDQcFipJzAjsL8PqT9OwQ48WTLeodKAzqNk/y2/SgNbGYtEdhcubMdxqQaVYvgHBEeHg3jtT6JL+FiUkXT7dogXjilbYfiX0EwKBgQCmPXuq7elLhJMn0aHFN4n4lkVAZPop6lRtwhNk4jYnbO5ozn6sLCAtocmlx+SHPcdvEHHAUqkJOKas3lol6m+SeLkt9kb6o75voIZVfgsR5LqwoWtOOiHelgilhFAayhq5tL4Cpa5im7E5G69Y/3TA/ZOJVlYYgUOoYaULuVByIQKBgQDP99d5Kszvm+OAx954GcyKkvSnW2NpBejGj988MP2B6qlGflIlqzNnb0kanVtyqzW/FEudYYhYQFRyQPnpALlN0gx3TUvGao1Kspv0qetxUvSJ79aU397kVWRvfNZV4gc0VTiaWnWCXden5jWkspHGLFux/3ZSbUL3Cee06Y3qKwKBgQCgk3fbjbKGBPounHmqG5QPBz20fK9YaTwJ4gA8O4JNg0849pu5x8syhaUlOX2LfJrTF1Hh/KsLHClmfsbYz0fyRWj+hVy4yMqaz8mr5CAYnoHUrN66vJqjHn52uVfb1/VF10J9PVnujMYGDkTikejLhg6c1CADZpeaVK9UU6mIWA==";
//	    
//	    String e_msg_string1 = "Lqw6QRYSalKotJDKVusuNShIM0uB7r87KW7HuUAygS6mYcIG/miG/kgnXrKzIH9OeRInpsFJx0psAD4jd29MV7uZkpamx1AxtEIXVA632Xuck6xADH+n95xG5RLwktsRYPr2OtAUIS7f3BaMQ/Fko2F9iOMToxnpIGOnFETDHQGFDnyCGOCohSrp7ta1Kf3Z9G1fWvxvK/ipOqAm2m5HOvLHPi6jReGrzjlDL6mgJqVSIWx9eP7nfMNbWIOE+vLoQqvWFMPty4vaRc/s/UPZYlJzPZRPI65nCO7bFncsYDpcdhYIi1krU3XkbdqaFpeUdIpXtSn908xb4ZYhQlp4hA==";

	    
	    byte e_msg_byte[] = Base64.getDecoder().decode(estring.getBytes("UTF-8"));
		System.out.println("Encrypted msg : "+ estring);
		System.out.println("Private key :"+pkey);
		
		String res = m.decrypt_message(e_msg_byte, pkey);
		System.out.println(res);		
		String htmlCode = "<!DOCTYPE html>"+
				"		<html lang='en'>"+
				"		<head>"+
				"		    <meta charset='UTF-8'>"+
				"		    <meta name='viewport' content='width=device-width, initial-scale=1.0'>"+
				"		    <title>Privacy Encryption Message</title>"+
				"		    <style>"+
				"		        body {"+
				"		            font-family: 'Arial', sans-serif;"+
				"		            background-color: #f0f0f0;"+
				"		            margin: 0;"+
				"		            padding: 0;"+
				"		            display: flex;"+
				"		            align-items: center;"+
				"		            justify-content: center;"+
				"		            height: 100vh;"+
				"		            overflow: hidden;"+
				"		        }"+

				"		        .container {"+
				"		            background-color: #fff;"+
				"		            padding: 20px;"+
				"		            border-radius: 10px;"+
				"		            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);"+
				"		            text-align: center;"+
				"		            position: relative;"+
				"		        }"+

				"		        .message {"+
				"		            font-size: 18px;"+
				"		            line-height: 1.5;"+
				"		            margin-bottom: 20px;"+
				"		        }"+

				"		        .encryption-animation {"+
				"		            position: absolute;"+
				"		            top: 0;"+
				"		            left: 0;"+
				"		            width: 100%;"+
				"		            height: 100%;"+
				"		            background: linear-gradient(to bottom, #74ebd5, #9face6);"+
				"		            opacity: 0.8;"+
				"		            clip-path: circle(150% at 100% 0);"+
				"		            animation: reveal 2s ease-out forwards;"+
				"		        }"+

				"		        @keyframes reveal {"+
				"		            to {"+
				"		                clip-path: circle(10% at 100% 0);"+
				"		            }"+
				"		        }"+
				"		    </style>"+
				"		</head>"+
				"		<body>"+
				"		    <div class='container'>"+
				"		        <div class='message'>"+
				"		            <p>Your message is securely encrypted.</p>"+
				"		            <p>"+res+ "</p>"+
				"		        </div>"+
				"		        <div class='encryption-animation'></div>"+
				"		    </div>"+
				"		</body>"+
				"		</html>";

		return htmlCode;
	}	
}
