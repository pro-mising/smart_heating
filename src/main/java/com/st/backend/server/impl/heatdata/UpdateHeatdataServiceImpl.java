package com.st.backend.server.impl.heatdata;

import com.st.backend.mapper.HeatdataMapper;
import com.st.backend.pojo.Heatdata;
import com.st.backend.pojo.User;
import com.st.backend.server.heatdata.UpdateHeatdataService;
import com.st.backend.server.impl.utils.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UpdateHeatdataServiceImpl implements UpdateHeatdataService {
    @Override
    public Map<String, String> update(Map<String, String> data) {
        return Map.of();
    }

//    @Autowired
//    private HeatdataMapper heatdataMapper;
//
//    @Override
//    public Map<String, String> update(Map<String, String> data) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
//        User user = loginUser.getUser();
//
//        int id = Integer.parseInt(data.get("id"));
//        Double roomTemperature = Double.valueOf(data.get("roomTemperature"));
//        Double humidity = Double.valueOf(data.get("humidity"));
//        String dateStr = data.get("datetime"); // e.g., "2025-05-06 10:30:00"
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        LocalDateTime localDateTime = LocalDateTime.parse(dateStr, formatter);
//
//// 设置时区为 Asia/Shanghai
//        ZoneId zoneId = ZoneId.of("Asia/Shanghai");
//        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
//
//// 转换为 java.util.Date
//        Date date = Date.from(zonedDateTime.toInstant());
//
//        String address = data.get("address");
//        Double secondTemperature = Double.valueOf(data.get("secondTemperature"));
//        Double firstTemperature = Double.valueOf(data.get("firstTemperature"));
//        Double wind = Double.valueOf(data.get("wind"));
//        String equipmentNumber = data.get("equipmentNumber");
//
//        Map<String, String> map = new HashMap<>();
//        Heatdata new_heatdata = new Heatdata(id,
//                roomTemperature, humidity, date,
//                address, secondTemperature, firstTemperature,
//                wind, equipmentNumber);
//
//        heatdataMapper.updateById(new_heatdata);
//        map.put("error_message", "success");
//        return map;
//    }
}