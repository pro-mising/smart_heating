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

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GetPredictServiceImpl implements GetPredictService {

    @Autowired
    private PredictMapper predictMapper;

    @Override
    public List<Predict> getPredictList(String address) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loggedUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loggedUser.getUser();

        Map<String,String> map = new HashMap<>();

        QueryWrapper<Predict> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("address",address);
        List<Predict> predicts = predictMapper.selectList(queryWrapper);
        System.out.println(predicts);

        return predicts;
    }
}
