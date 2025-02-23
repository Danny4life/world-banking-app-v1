package com.osiki.World_Banking_Application.service;

import com.osiki.World_Banking_Application.domain.entity.UserEntity;
import com.osiki.World_Banking_Application.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<UserEntity> userEntityOptional = isEmail(username)
                ? userRepository.findByEmail(username)
                : userRepository.findByPhoneNumber(normalizedPhoneNumber(username));


        return userEntityOptional.get();
    }

    private String normalizedPhoneNumber(String phoneNumber) {
        return phoneNumber.replaceAll("[^0-9]", "");
    }

    private boolean isEmail(String input) {
        return input.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}
