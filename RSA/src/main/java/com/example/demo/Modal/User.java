package com.example.demo.Modal;

import org.springframework.stereotype.Component;

import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Data;

@Component
@Entity
@Data
public class User {
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private long id;
	private String sender_email;
	private String encrypted_msg1;
	private String encrypted_msg2;
	private String private_key1;
	private String private_key2;
	private String private_key3;
	private String private_key4;
	private String private_key5;
	private String private_key6;
	private String private_key7;

}
