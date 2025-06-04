package com.st.backend.server.user;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface LoginService {
    public Map<String, String> getToken(String name, String password);
}
