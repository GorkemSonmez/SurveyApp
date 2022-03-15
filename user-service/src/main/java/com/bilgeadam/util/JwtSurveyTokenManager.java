package com.bilgeadam.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class JwtSurveyTokenManager {

    @Value("${surveyapp.secretkey}")
    private String secretkey;


    public Optional<String> createToken(String userId,String surveyId,long expireAfter) {
        String token;
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretkey);

            token = JWT.create()
                    .withAudience()
                    .withClaim("userId", userId)
                    .withClaim("surveyId", surveyId)
                    .withIssuer("bilgeadam.com")
                    .withExpiresAt(new Date(expireAfter))
                    .withIssuedAt(new Date())
                    .sign(algorithm);
            return Optional.of(token);
        }catch (Exception e){
            return Optional.empty();
        }
    }
    public boolean validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretkey);

            JWTVerifier jwtVerifier =  JWT.require(algorithm)
                    .withIssuer("bilgeadam.com")
                    .build();

            DecodedJWT decode = jwtVerifier.verify(token);
            if (decode==null) return false;
            return true;
        }catch (Exception e){
            return false;
        }
    }
    public Optional<String> getUserId(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretkey);
            JWTVerifier jwtVerifier =  JWT.require(algorithm)
                    .withIssuer("bilgeadam.com")
                    .build();
            DecodedJWT decode = jwtVerifier.verify(token);
            if (decode==null) return Optional.empty();
            String userId = decode.getClaim("userId").asString();
            return Optional.of(userId);
        }catch (Exception e){
            return Optional.empty();
        }
    }
    public Optional<String> getSurveyId(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretkey);
            JWTVerifier jwtVerifier =  JWT.require(algorithm)
                    .withIssuer("bilgeadam.com")
                    .build();
            DecodedJWT decode = jwtVerifier.verify(token);
            if (decode==null) return Optional.empty();
            String surveyId = decode.getClaim("surveyId").asString();
            return Optional.of(surveyId);
        }catch (Exception e){
            return Optional.empty();
        }
    }
}