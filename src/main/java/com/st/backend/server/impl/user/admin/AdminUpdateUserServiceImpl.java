package com.st.backend.server.impl.user.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.st.backend.mapper.UserMapper;
import com.st.backend.pojo.User;
import com.st.backend.server.impl.utils.UserDetailsImpl;
import com.st.backend.server.user.admin.AdminUpdateUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AdminUpdateUserServiceImpl implements AdminUpdateUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Map<String, String> update(String username, String realname, String phone, String email, String department) {
        UsernamePasswordAuthenticationToken authenticationToken
                = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        Map<String,String> map = new HashMap<>();
        if(user.getFlag() == 0) {
            map.put("error_message", "您没有权限修改用户信息");
            return map;
        }

        User user1 = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
        if(username == null) {
            map.put("error_message", "用户名不能为空");
            return map;
        }

        username = username.trim(); //去掉名字前后的空格


        if(username.isEmpty()) {
            map.put("error_message", "用户名并不能为空");
            return map;
        }


        if(username.length() > 100) {
            map.put("error_message", "用户名长度不能大于100");
            return map;
        }
        String photo = user1.getPhoto();
        User new_user = new User(user1.getId(), username, realname, user1.getPassword(), photo, phone, email, department, 0);
        userMapper.updateById(new_user);
        map.put("error_message", "success");
        return map;
    }
}
