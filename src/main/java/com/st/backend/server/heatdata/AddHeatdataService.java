package com.st.backend.server.heatdata;

import java.util.Date;
import java.util.Map;

public interface AddHeatdataService {
    public Map<String, String> add(Double roomTemperature,
                                   Double humidity, Date date, String address, Double secondTemperature, Double firstTemperature, Double wind, String equipmentNumber);
}
