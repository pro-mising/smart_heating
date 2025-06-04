package com.st.backend.controller.user.admin;

import com.st.backend.pojo.User;
import com.st.backend.server.user.admin.GetUserListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetUserListController {

    @Autowired
    private GetUserListService getUserListService;

    @GetMapping("/user/admin/getuserlist")
    public List<User> getUserList(){
        return getUserListService.getUserList();
    }
}
