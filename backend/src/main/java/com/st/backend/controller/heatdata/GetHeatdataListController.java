package com.st.backend.controller.heatdata;

import com.st.backend.mapper.HeatdataMapper;
import com.st.backend.pojo.Heatdata;
import com.st.backend.server.heatdata.GetHeatdataListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class GetHeatdataListController {

    @Autowired
    private GetHeatdataListService getHeatdataListService;

    @GetMapping("/heatdata/getbydate")
    public List<Heatdata> getHeatdataListByDate(@RequestParam(value = "date", required = false) String date) {
        System.out.println("heatdata ListByDate controller success");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = null;
        try {
            date1 = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

        return getHeatdataListService.getListByDate(date1);
    }

    @GetMapping("/heatdata/getbyaddress")
    public List<Heatdata> getHeatdataListByAddress(@RequestParam(value = "address", required = false) String address) {
        System.out.println("heatdata ListByAddress controller success");

        return getHeatdataListService.getListByAddress(address);
    }

    @GetMapping("/heatdata/getbyequipmentnumber")
    public List<Heatdata>  getHeatdataListByEquipmentNumber(@RequestParam(value = "equipmentNumber", required = false) String equipmentNumber) {
        System.out.println("heatdata ListByEquipmentNumber controller success");

        return getHeatdataListService.getListByEquipmentNumber(equipmentNumber);
    }
}
