package com.st.backend.server.heatdata;

import com.alibaba.fastjson2.JSONObject;

public interface GetHeatdataList2Service {
    JSONObject getList(Integer page, String address);
}
