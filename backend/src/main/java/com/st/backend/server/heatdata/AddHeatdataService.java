package com.st.backend.server.heatdata;

import java.util.Date;
import java.util.Map;

public interface AddHeatdataService {
    public Map<String, String> add(String address, Date date, Double indoorTemperature, Double outdoorTemperature, Double heatLoad, String wind);
}
