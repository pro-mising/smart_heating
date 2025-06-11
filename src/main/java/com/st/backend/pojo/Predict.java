package com.st.backend.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Predict {
    @TableId(type = IdType.AUTO) //ID自增
    private Integer id;
    private String address;
    private Double heatLoad;
    private Double loss;
    private Double accuracy;
    private Double rmse;
    private String time;
}
