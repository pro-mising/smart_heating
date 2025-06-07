package com.st.backend.controller.predict;

import com.st.backend.server.predict.GetPredictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GetPredictController {

    @Autowired
    private GetPredictService getPredictService;

    @GetMapping("/predict/get")
    public Map<String,String> getPredict(@RequestParam Map<String,String> data){
        System.out.println("getPredict controller success");
        String address = data.get("address");
        String time = data.get("time");
        return getPredictService.getPredictList(address, time);
    }
}
