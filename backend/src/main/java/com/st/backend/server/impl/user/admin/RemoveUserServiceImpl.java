package com.st.backend.server.impl.user.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.st.backend.mapper.UserMapper;
import com.st.backend.pojo.User;
import com.st.backend.server.impl.utils.UserDetailsImpl;
import com.st.backend.server.user.admin.RemoveUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RemoveUserServiceImpl implements RemoveUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Map<String, String> removeUser(int id) {
        UsernamePasswordAuthenticationToken authenticationToken
                = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        Map<String,String> map = new HashMap<>();
        if(user.getFlag() == 0) {
            map.put("error_message", "很抱歉，您没有权限操作");
            return map;
        }



        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        User user1 = userMapper.selectOne(queryWrapper);
        if(user1==null) {
            map.put("error_message", "用户不存在或者已被删除");
            return map;
        }
        userMapper.delete(queryWrapper);
        map.put("error_message", "success");
        return map;
    }
}
