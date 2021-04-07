package com.xc.mail.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api("xxx功能")
public class HelloController {

    @ApiOperation("做什么功能")
    @ApiImplicitParam(name = "参数名称",value = "参数值",dataType = "String")
    @RequestMapping("/test")
    public String testHello(){
        return "hello";
    }
}
