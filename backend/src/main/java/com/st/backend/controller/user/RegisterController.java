package com.st.backend.controller.user;

import com.st.backend.server.user.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RegisterController {
    @Autowired
    private RegisterService registerService;

    @PostMapping("/user/register")
    public Map<String, String> register(@RequestParam Map<String, String> map) {
        String username = map.get("username");
        String password = map.get("password");
        String confirmPassword = map.get("confirmPassword");
        String email = map.get("email");
        String phone = map.get("phone");
        String realname = map.get("realname");
        String department  = map.get("department");
        System.out.println("registercontroller success");
        return registerService.getToken(username,realname, password, confirmPassword, phone, email, department);
    }
}
