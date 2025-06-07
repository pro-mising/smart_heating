package com.st.backend.server.user.admin;

import java.util.Map;

public interface AddUserService {
    public Map<String,String> addUser(String username, String realname, String password, String photo, String email, String department);
}
