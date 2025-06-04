package com.st.backend.server.predict;

import com.st.backend.pojo.Predict;

import java.util.Map;

public interface AddPredictService {
    public Map<String,Object> addPredict(Map<String,String> data);
}
