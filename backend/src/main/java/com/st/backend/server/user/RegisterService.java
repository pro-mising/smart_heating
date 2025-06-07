package com.st.backend.server.user;

import java.util.Map;

public interface RegisterService {
    public Map<String, String> getToken(String username,String realname, String password, String confirmPassword,String  phone, String email,String department);
}
