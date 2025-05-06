package com.st.backend.server.impl.heatdata;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.st.backend.mapper.HeatdataMapper;
import com.st.backend.pojo.Heatdata;
import com.st.backend.pojo.User;
import com.st.backend.server.heatdata.RemoveHeatdataService;
import com.st.backend.server.impl.utils.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
;
import java.util.HashMap;
import java.util.Map;

@Service
public class RemoveHeatdataServiceImpl implements RemoveHeatdataService {

    @Autowired
    private HeatdataMapper heatdataMapper;

    @Override
    public Map<String, String> remove(Map<String, String> data) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        User user = loginUser.getUser();

        Map<String,String> map = new HashMap<>();

        int id = Integer.parseInt(data.get("id"));
        QueryWrapper<Heatdata>  queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        heatdataMapper.delete(queryWrapper);
        map.put("error_message", "success");
        return map;

    }
}
