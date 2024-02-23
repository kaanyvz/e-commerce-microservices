package com.ky.userservice.service.security;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class LoginService {
    private final int maxAttempts = 5;
    private final LoadingCache<String, Integer> loginCache;

    public LoginService(){
        super();
        loginCache = CacheBuilder.newBuilder().expireAfterWrite(15, TimeUnit.MINUTES)
                .maximumSize(100)
                .build(new CacheLoader<String, Integer>() {
                    public Integer load(String key) {
                        return 0;
                    }
                });
    }

    public void addLoginCache(String email){
        int attempts = 0;
        try {
            attempts = (loginCache.get(email) + 1);
        }catch (ExecutionException e){
            e.printStackTrace();
        }
        loginCache.put(email, attempts);
    }

    public void removeLoginCache(String email){
        loginCache.invalidate(email);
    }

    public boolean isExceededMaxAttempts(String email){
        try {
            return loginCache.get(email) >= 5;
        }catch (ExecutionException e){
            e.printStackTrace();
        }
        return false;
    }
}
