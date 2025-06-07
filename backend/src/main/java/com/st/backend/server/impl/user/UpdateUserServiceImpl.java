package com.st.backend.server.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.st.backend.mapper.UserMapper;
import com.st.backend.pojo.User;
import com.st.backend.server.impl.utils.UserDetailsImpl;
import com.st.backend.server.user.UpdateUserService;
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

        String username = data.get("username");
        String photo = data.get("photo");
        String realname = data.get("realname");
        String department  = data.get("department");
        String phone = data.get("phone");
        String email = data.get("email");

        Map<String, String> map = new HashMap<>();

        QueryWrapper<User> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("id", user.getId());
        User user2 = userMapper.selectOne(queryWrapper1);
//        if(user2.getFlag() == 0) {
//            map.put("error_message", "很抱歉，您没有操作权限");
//            return map;
//        }

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

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user1 = userMapper.selectOne(queryWrapper);

        User new_user = new User(user.getId(), username, realname, user.getPassword(), photo, phone, email, department, 0);
        userMapper.updateById(new_user);
        map.put("error_message", "success");
        return map;
    }
}
