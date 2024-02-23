package com.example.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SendEmail {
	@Autowired
	JavaMailSender sender;
	public void sendmail(String to,String sub,String body) {
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(to);
		mail.setFrom("rsaencryptyourmessage@gmail.com");
		mail.setSubject(sub);
		mail.setText(body);		
		sender.send(mail);
	}

}
