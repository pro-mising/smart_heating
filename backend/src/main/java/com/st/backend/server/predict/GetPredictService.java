package com.st.backend.server.predict;

import java.util.Map;

public interface GetPredictService {
    public Map<String, String> getPredictList(String address, String time);
}
