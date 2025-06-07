package com.st.backend.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @TableId(type = IdType.AUTO) //ID自增
    private Integer id;
    private String username;
    private String realname;
    private String password;
    private String photo;
    private String phone;
    private String email;
    private String department;
    private Integer flag;
}
