package com.st.backend.controller.heatdata;

import com.alibaba.fastjson2.JSONObject;
import com.st.backend.server.heatdata.GetHeatdataList2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GetHeatDataList2Controller {

    @Autowired
    private GetHeatdataList2Service getHeatdataList2Service;

    @GetMapping("/heatdata/getlist")
    public JSONObject getList(@RequestParam Map<String,String> data) {
        System.out.println("11111111111111111");
        Integer page =  Integer.parseInt(data.get("page"));
        String  address = data.get("address");
//        Integer  page = Integer.parseInt(data.get("page"));

        return getHeatdataList2Service.getList(page,address);
    }
}
