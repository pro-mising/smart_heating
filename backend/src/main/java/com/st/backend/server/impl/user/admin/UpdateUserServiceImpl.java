package com.st.backend.server.impl.user.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.st.backend.mapper.UserMapper;
import com.st.backend.pojo.User;
import com.st.backend.server.impl.utils.UserDetailsImpl;
import com.st.backend.server.user.admin.UpdateUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UpdateUserServiceImpl implements UpdateUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> updateInfo(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authenticationToken
                = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        int user_id = Integer.parseInt(data.get("id"));
        String username = data.get("username");
        String password = data.get("password");
        String photo = data.get("photo");

        Map<String, String> map = new HashMap<>();

        QueryWrapper<User> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("id", user.getId());
        User user2 = userMapper.selectOne(queryWrapper1);
        if(user2.getFlag() == 0) {
            map.put("error_message", "很抱歉，您没有操作权限");
            return map;
        }


        if(username == null) {
            map.put("error_message", "用户名不能为空");
            return map;
        }

        username = username.trim(); //去掉名字前后的空格

        if(password == null) {
            map.put("error_messge", "密码不能为空");
            return map;
        }


        if(username.isEmpty()) {
            map.put("error_message", "用户名并不能为空");
            return map;
        }

        if(password.isEmpty()) {
            map.put("error_message", "密码不能为空");
            return map;
        }

        if(username.length() > 100) {
            map.put("error_message", "用户名长度不能大于100");
            return map;
        }

        if(password.length() > 100) {
            map.put("error_message", "密码长度不能大于100");
            return map;
        }


        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user1 = userMapper.selectOne(queryWrapper);
        if(!user1.getPassword().equals(password)) {
            String encodePassword = passwordEncoder.encode(password);
            password = encodePassword;
        }

        User new_user = new User(user_id, username, password, photo, 0);
        userMapper.updateById(new_user);
        map.put("error_message", "success");
        return map;
    }
}
