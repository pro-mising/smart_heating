package com.st.backend.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8") // 控制前后端交互的格式
    private Date date;
    private Double hourOne;
    private Double hourSeven;
    private Double hourTwentyfour;
    private Double mae;
    private Double rmse;
    private Double rTwo;
}
