package com.st.backend.server.impl.user.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.st.backend.mapper.UserMapper;
import com.st.backend.pojo.User;
import com.st.backend.server.impl.utils.UserDetailsImpl;
import com.st.backend.server.user.admin.AddUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AddUserServiceImpl implements AddUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> addUser(String username, String realname, String password, String phone, String email, String department) {
        UsernamePasswordAuthenticationToken authenticationToken
                = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user1 = loginUser.getUser();

        Map<String, String> map = new HashMap<>();
        if(username == null) {
            map.put("error_message", "用户名不能为空");
            return map;
        }

        if(user1.getFlag()  == 0) {
            map.put("error_message", "没有权限进行此操作");
            return map;
        }

        username = username.trim(); //去掉名字前后的空格

        if(password == null) {
            map.put("error_message", "密码不能为空");
            return map;
        }

        if(realname == null) {
            map.put("error_message", "真实姓名不能为空");
            return map;
        }

        if(phone == null) {
            map.put("error_message", "电话不能为空");
            return map;
        }

        if(department == null) {
            map.put("error_message", "所属部门不能为空");
            return map;
        }

        if(username.isEmpty()) {
            map.put("error_message", "用户名并不能为空");
            return map;
        }

        if(realname.isEmpty()) {
            map.put("error_message", "真实姓名不能为空");
            return map;
        }

        if(password.isEmpty()) {
            map.put("error_message", "密码不能为空");
            return map;
        }

        if(phone.isEmpty()) {
            map.put("error_message", "电话不能为空");
            return map;
        }

        if(department.isEmpty()) {
            map.put("error_message", "所属部门不能为空");
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
        List<User> users = userMapper.selectList(queryWrapper);
        if(!users.isEmpty()) {
            map.put("error_message", "用户名已存在");
            return map;
        }

        String encodePassword = passwordEncoder.encode(password);
        String photo = "https://cdn.acwing.com/media/user/profile/photo/145584_lg_990c1295e1.png";
        User user = new User(null, username, realname, encodePassword, photo, phone, email, department,0);
        userMapper.insert(user);
        map.put("error_message", "success");
        return map;
    }
}
