package com.st.backend.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date predictTime;
    private String time;
}
