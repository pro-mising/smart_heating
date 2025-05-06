package com.st.backend.server.impl.predict;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.st.backend.mapper.PredictMapper;
import com.st.backend.pojo.Predict;
import com.st.backend.pojo.User;
import com.st.backend.server.impl.utils.UserDetailsImpl;
import com.st.backend.server.predict.GetPredictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GetPredictServiceImpl implements GetPredictService {

    @Autowired
    private PredictMapper predictMapper;

    @Override
    public Map<String, String>getPredictList(int id) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loggedUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loggedUser.getUser();

        Map<String,String> map = new HashMap<>();

        QueryWrapper<Predict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        Predict predict = predictMapper.selectOne(queryWrapper);
        if(predict!=null){
            map.put("secondTemperature", String.valueOf(predict.getSecondTemperature()));
            map.put("heat",  String.valueOf(predict.getHeat()));
            map.put("error_message", "success");
            return map;
        }

        map.put("error_message", "没有查询到此条消息");
        return map;
    }
}
