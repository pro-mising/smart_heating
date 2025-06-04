package com.st.backend.server.impl.heatdata;

import com.st.backend.mapper.HeatdataMapper;
import com.st.backend.pojo.Heatdata;
import com.st.backend.pojo.User;
import com.st.backend.server.heatdata.AddHeatdataService;
import com.st.backend.server.impl.utils.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AddHeatdataServiceImpl implements AddHeatdataService {
    @Autowired
    private HeatdataMapper heatdataMapper;

    @Override
    public Map<String, String> add(Double roomTemperature,
                                   Double humidity, Date date, String address, Double secondTemperature, Double firstTemperature, Double wind, String equipmentNumber) {
        Map<String, String> map = new HashMap<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        User user = loginUser.getUser();

        Heatdata heatdata = new Heatdata(null, roomTemperature, humidity, date,
                address, secondTemperature, firstTemperature,wind, equipmentNumber);
        heatdataMapper.insert(heatdata);

        System.out.println("Add HeatdataService success");
        map.put("error_message", "success");
        return map;
    }
}
