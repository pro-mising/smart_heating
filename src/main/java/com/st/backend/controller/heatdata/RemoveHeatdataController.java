package com.st.backend.controller.heatdata;

import com.st.backend.server.heatdata.RemoveHeatdataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RemoveHeatdataController {

    @Autowired
    private RemoveHeatdataService removeHeatdataService;

    @PostMapping("/heatdata/remove")
    public Map<String, String> removeHeatdata(@RequestParam Map<String, String> data) {
        System.out.println("removeHeatdata success");
        return removeHeatdataService.remove(data);
    }
}
