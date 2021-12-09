package com.maplexl.shiroJwtRedis.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * user
 * @author 
 */
@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    /**
     * 用户id主键
     */
    @ApiModelProperty("用户id主键")
    @TableId(value = "user_id", type = IdType.ASSIGN_ID)
    private int userId;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String userName;

    /**
     * 密码
     */
    @ApiModelProperty("密码")
    private String password;
    /**
     * 加密使用的盐值
     */
    @ApiModelProperty(value = "盐值",hidden = true)
    private String salt;

    /**
     * 性别0表示女1表示男
     */
    @ApiModelProperty("性别0表示女1表示男")
    private Integer sex;

    /**
     * 生日
     */
    @ApiModelProperty(value = "生日",dataType = "java.util.date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    /**
     * 权限2表示查看，3表示查看和修改，4表示可授权，6(2+4)表示可授予其他人查看
     */
    @ApiModelProperty("权限")
    private Integer authority;

    private static final long serialVersionUID = 917914644700317564L;
}