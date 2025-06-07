package com.st.backend.server.impl.heatdata;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.st.backend.mapper.HeatdataMapper;
import com.st.backend.pojo.Heatdata;
import com.st.backend.server.heatdata.GetHeatdataList2Service;
import com.st.backend.server.impl.utils.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetHeatdataList2ServiceImpl implements GetHeatdataList2Service {
    
    @Autowired
    private HeatdataMapper heatdataMapper;


    @Override
    public JSONObject getList(Integer page, String address) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        List<Heatdata> heatdata = null;
        QueryWrapper<Heatdata> queryWrapper = new QueryWrapper<>();
        IPage<Heatdata> userIPage = new Page<>(page, 10);

        if (address != null && !address.isEmpty()) {
            queryWrapper.eq("address", address);
        }

        // 无论是否添加条件，统一调用
        heatdata = heatdataMapper.selectPage(userIPage, queryWrapper).getRecords();
        JSONObject resp = new JSONObject();
        resp.put("heatdata", heatdata);
        resp.put("heatdata_count", heatdataMapper.selectCount(null));
        return resp;
    }
}
