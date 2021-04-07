package com.xc.mail.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author admin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("用户")
public class User {

    @ApiModelProperty("用户id")
    private String id;
    @ApiModelProperty("用户名称")
    private  String name;
    @ApiModelProperty("用户年龄")
    private  String age;
}
