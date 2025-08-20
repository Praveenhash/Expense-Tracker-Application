package org.example.service;

import org.example.entities.RefreshToke;
import org.example.entities.UserInfo;
import org.example.repository.RefreshTokenRepository;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserRepository userRepository;

    public RefreshToke createRefreshToken(String username){
        UserInfo UserInfoExtracted = userRepository.findByUsername(username);
        RefreshToke refreshToken = RefreshToke.builder()
                .userInfo(UserInfoExtracted)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(600000))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToke verifyExpiration(RefreshToke token){
        if(token.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken()+" Refresh toke is expired. Please make a new login...!");
        }
        return token;
    }

    public Optional<RefreshToke> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }
}
