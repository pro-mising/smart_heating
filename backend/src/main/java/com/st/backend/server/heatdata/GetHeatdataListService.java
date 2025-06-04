package com.st.backend.server.heatdata;

import com.st.backend.pojo.Heatdata;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface GetHeatdataListService {
    public List<Heatdata> getListByDate(Date date);
    public List<Heatdata> getListByAddress(String address);
    public List<Heatdata> getListByEquipmentNumber(String equipmentNumber);
}
