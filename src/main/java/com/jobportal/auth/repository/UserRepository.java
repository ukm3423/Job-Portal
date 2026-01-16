package com.jobportal.auth.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jobportal.auth.models.Role;
import com.jobportal.auth.models.User;


@Repository
public interface UserRepository extends JpaRepository<User , Long>{
    
	Optional<User> findByEmail(String email);
	
	List<User> findByRole(Role role);
	
	Long countByRole(Role role);

	Boolean existsByEmail(String email);
	
}