package com.st.backend.controller.heatdata;

import com.st.backend.server.heatdata.AddHeatdataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

@RestController
public class AddHeatdataController {

    @Autowired
    private AddHeatdataService addHeatdataService;

    @PostMapping("/heatdata/add")
    public Map<String, String> add(@RequestParam Map<String, String> data) {
        System.out.println("addheatdatacontroller success");
        Double roomTemperature = Double.parseDouble(data.get("roomTemperature"));
        Double Humidity = Double.parseDouble(data.get("humidity"));
//        String date =  data.get("datetime");

        String dateStr = data.get("datetime"); // e.g., "2025-05-06 10:30:00"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(dateStr, formatter);

// 设置时区为 Asia/Shanghai
        ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);

// 转换为 java.util.Date
        Date date = Date.from(zonedDateTime.toInstant());

        String address = data.get("address");
        Double secondTemperature = Double.parseDouble(data.get("secondTemperature"));
        Double firstTemperature = Double.parseDouble(data.get("firstTemperature"));
        Double wind = Double.parseDouble(data.get("wind"));
        String equipmentNumber = data.get("equipmentNumber");

        return addHeatdataService.add(roomTemperature, Humidity, date, address,secondTemperature, firstTemperature, wind,equipmentNumber);
    }
}
