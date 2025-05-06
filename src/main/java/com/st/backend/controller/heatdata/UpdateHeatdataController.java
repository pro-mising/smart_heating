package com.st.backend.controller.heatdata;

import com.st.backend.server.heatdata.UpdateHeatdataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UpdateHeatdataController {

    @Autowired
    private UpdateHeatdataService updateHeatdataService;

    @PostMapping("/heatdata/update")
    public Map<String, String> update(@RequestParam Map<String, String> data) {

        System.out.println("heatdatacontroller success");

        return updateHeatdataService.update(data);
    }

    @GetMapping("/test")
    public String test(@RequestParam Map<String, String> data) {

        System.out.println("heatdatacontroller success");

        return "success";
    }

}