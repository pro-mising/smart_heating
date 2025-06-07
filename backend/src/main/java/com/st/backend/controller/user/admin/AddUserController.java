package com.st.backend.controller.user.admin;

import com.st.backend.server.user.admin.AddUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AddUserController {

    @Autowired
    private AddUserService addUserService;

    @PostMapping("/user/admin/adduser")
    public Map<String,String> removeUser(@RequestParam Map<String,String> data){
        String username = data.get("username");
        String realname = data.get("realname");
        String password = data.get("password");
        String phone = data.get("phone");
        String email = data.get("email");
        String department = data.get("department");
        return addUserService.addUser(username, realname,  password, phone, email, department);
    }

}
