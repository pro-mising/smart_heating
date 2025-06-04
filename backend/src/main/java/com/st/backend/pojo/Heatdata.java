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
public class Heatdata {
    @TableId(type = IdType.AUTO) //ID自增
    private Integer id;
    private Double roomTemperature;
    private Double humidity;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date date;
    private String address;
    private Double secondTemperature;
    private Double firstTemperature;
    private Double wind;
    private String equipmentNumber;
}
