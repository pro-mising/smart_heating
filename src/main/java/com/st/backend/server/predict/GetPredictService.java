package com.st.backend.server.predict;

import com.st.backend.pojo.Predict;

import java.util.List;
import java.util.Map;

public interface GetPredictService {
    public List<Predict> getPredictList(String address);
}
