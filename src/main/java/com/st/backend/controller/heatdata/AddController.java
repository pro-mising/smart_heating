package com.st.backend.controller.heatdata;

import com.st.backend.server.heatdata.AddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AddController {

    @Autowired
    private AddService addService;

    @PostMapping("/heatdata/add/")
    public Map<String, String> add(@RequestParam Map<String, String> data) {
        System.out.println("add success");
        return addService.add(data);
    }
}
