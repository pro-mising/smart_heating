package com.st.backend.server.impl.heatdata;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.st.backend.mapper.HeatdataMapper;
import com.st.backend.pojo.Heatdata;
import com.st.backend.pojo.User;
import com.st.backend.server.heatdata.GetHeatdataListService;
import com.st.backend.server.impl.utils.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class GetHeatdataListServiceImpl implements GetHeatdataListService {

    @Autowired
    private HeatdataMapper heatdataMapper;

    @Override
    public List<Heatdata> getListByDate(Date date) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        User user = loginUser.getUser();

        QueryWrapper<Heatdata> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("date",date);
        List<Heatdata> list = heatdataMapper.selectList(queryWrapper);
        return list;
    }

    @Override
    public List<Heatdata> getListByAddress(String address) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        User user = loginUser.getUser();
        System.out.println("address = " + address);

        QueryWrapper<Heatdata> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("address",address);
        List<Heatdata> list = heatdataMapper.selectList(queryWrapper);
        return list;
    }

    @Override
    public List<Heatdata> getListByEquipmentNumber(String equipmentNumber) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        User user = loginUser.getUser();

        QueryWrapper<Heatdata> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("equipment_number",equipmentNumber);
        List<Heatdata> list = heatdataMapper.selectList(queryWrapper);
        return list;
    }
}
