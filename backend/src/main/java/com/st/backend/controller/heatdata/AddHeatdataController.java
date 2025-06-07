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
        String address = data.get("address");
        Double indoorTemperature = Double.parseDouble(data.get("indoor_Temperature"));
        Double outdoorTemperature = Double.parseDouble(data.get("outdoor_Temperature"));
        Double heatLoad = Double.parseDouble(data.get("heatLoad"));
        String wind = data.get("wind");
//        String date =  data.get("datetime");

        String dateStr = data.get("date"); // e.g., "2025-05-06 10:30:00"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(dateStr, formatter);

// 设置时区为 Asia/Shanghai
        ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);

// 转换为 java.util.Date
        Date date = Date.from(zonedDateTime.toInstant());

        return addHeatdataService.add(address, date, indoorTemperature, outdoorTemperature,heatLoad, wind);
    }
}
