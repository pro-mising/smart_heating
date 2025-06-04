package com.st.backend.controller.predict;

import com.st.backend.server.predict.AddPredictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AddPredictController {

    @Autowired
    private AddPredictService addPredictService;

    @PostMapping("/predict/add")
    public Map<String,Object> addPredict(@RequestParam Map<String,String> data){
        System.out.println("addPredict controller success");
        return addPredictService.addPredict(data);
    }
}
