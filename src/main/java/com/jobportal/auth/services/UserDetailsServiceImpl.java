package com.jobportal.auth.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jobportal.auth.models.User;
import com.jobportal.auth.repository.UserRepository;
import com.jobportal.common.dto.ErrorCode;
import com.jobportal.common.exceptions.BusinessException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		User user = userRepo.findByEmail(email)
				.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_REGISTERED, "User is not registered"));

		if (!user.isActive()) {
			throw new BusinessException(ErrorCode.USER_INACTIVE, "User account is inactive");
		}

		return buildUserDetails(user);
	}

	private UserDetails buildUserDetails(User user) {

		return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
				.password(user.getPassword()).authorities("ROLE_" + user.getRole().name()).accountLocked(false)
				.accountExpired(false).credentialsExpired(false).disabled(!user.isActive()).build();
	}
}
