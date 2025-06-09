package com.st.backend.server.user.admin;

import java.util.Map;

public interface AdminUpdateUserService {
    public Map<String, String> update(String username,String realname, String phone, String email, String department);
}
