package io.khaminfo.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import io.khaminfo.ppmtool.domain.User;
import io.khaminfo.ppmtool.exceptions.UserNameExistsException;
import io.khaminfo.ppmtool.repositories.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public User saveUser(User newUser) {
		
		try {
		newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
		 newUser.setConfirmPassword("");
		return userRepository.save(newUser);
		}catch(Exception e) {
			throw new UserNameExistsException("userName '"+newUser.getUsername()+"' Already Exists");
		}
	}
}
