package com.st.backend.server.impl.predict;

import com.st.backend.mapper.PredictMapper;
import com.st.backend.pojo.Predict;
import com.st.backend.pojo.User;
import com.st.backend.server.impl.utils.UserDetailsImpl;
import com.st.backend.server.predict.AddPredictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AddPredictServiceImpl implements AddPredictService {

    @Autowired
    private PredictMapper predictMapper;

    @Override
    public Map<String, Object> addPredict(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        Map<String,Object> map = new HashMap<>();
        Double secondTemperature = Double.parseDouble(data.get("secondTemperature"));
        Double heat = Double.parseDouble(data.get("heat"));
        Predict new_predict = new Predict(null, secondTemperature, heat);
        predictMapper.insert(new_predict);
        map.put("error_message", "success");
        map.put("id", new_predict.getId());  // 获取自动生成的主键 ID
        return map;
    }
}
