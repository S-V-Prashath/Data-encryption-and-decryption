package com.example.demo.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.example.demo.Modal.User;
import com.example.demo.Repositary.UserRepositary;

@Service
public class UserService {
	
	@Autowired
	UserRepositary urepo;
	
	public void saveUser(User user) {
		urepo.save(user);
	}
	
	public ArrayList<User> fetchByEmailAndPk(String semail,String pkey){
		return urepo.fetchByemailandpk(semail, pkey);
		//return urepo.fetchByemailandpk(semail);
	}
}
