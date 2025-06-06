package com.st.backend.controller.user;

import com.st.backend.server.user.UpdateUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UpdateUserController {

    @Autowired
    private UpdateUserService updateUserService;

    @PostMapping("/user/updateuser")
    public Map<String, String>  updateUserInfo(@RequestParam Map<String,String> data) {
            System.out.println("user admin updateUserInfo");
            return updateUserService.updateInfo(data);
    }

}
