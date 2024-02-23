package com.example.demo.Repositary;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.example.demo.Modal.User;

public interface UserRepositary extends JpaRepository<User, Long> {

	@Query(value="select u from User u where sender_email=:se and private_key1=:pk ")
	public ArrayList<User> fetchByemailandpk(@Param("se")String semail ,@Param("pk") String pri_key);
	//public List<User> fetchByemailandpk(@Param("se")String semail );
}
