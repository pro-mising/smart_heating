package com.st.backend.controller.user.admin;


import com.st.backend.server.user.admin.AdminUpdateUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AdminUpdateUserController {

    @Autowired
    private AdminUpdateUserService adminUpdateUserService;

    @PostMapping("/user/admin/updateuser")
    public Map<String, String> updateUser(@RequestParam Map<String, String> data){
        System.out.println("updateUser success");
        String username = data.get("username");
        String realname = data.get("realname");
        String phone = data.get("phone");
        String email = data.get("email");
        String department = data.get("department");
        return adminUpdateUserService.update(username, realname, phone, email, department);
    }
}
