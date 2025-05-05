package com.st.backend.server.impl.heatdata;

import com.st.backend.mapper.HeatdataMapper;
import com.st.backend.pojo.User;
import com.st.backend.server.heatdata.AddService;
import com.st.backend.server.impl.utils.UserDetailsImpl;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AddServiceImpl implements AddService {
    @Autowired
    private HeatdataMapper heatdataMapper;

    @Override
    public Map<String, String> add(Map<String, String> data) {
        Map<String, String> map = new HashMap<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        User user = loginUser.getUser();


        System.out.println("Add HeatdataService success");
        return null;
    }
}
