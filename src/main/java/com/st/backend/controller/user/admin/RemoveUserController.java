package com.st.backend.controller.user.admin;

import com.st.backend.server.user.admin.RemoveUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RemoveUserController {
    @Autowired
    private RemoveUserService removeUserService;

    @PostMapping("/user/admin/removeuser")
    public Map<String,String> removeUser(@RequestParam Map<String,String> data){
        String username = data.get("username");
        return removeUserService.removeUser(username);
    }
}
