package ch.ese.team6.service;

import java.util.HashSet;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import ch.ese.team6.models.users.RoleRepository;
import ch.ese.team6.models.users.UserRepository;
import ch.ese.team6.models.users.Users;

@Service
public class UserServiceImpl implements UserService  {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public void save(Users user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setRoles(new HashSet<>(roleRepository.findAll()));
		userRepository.save(user);
	}
	
	@Override
	public Users findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
}
