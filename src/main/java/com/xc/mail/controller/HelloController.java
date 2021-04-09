package com.xc.mail.controller;

import com.xc.mail.service.GoodsStoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@RestController
@Api("xxx功能")
@RequestMapping("/")
public class HelloController {

    @Resource
    private GoodsStoreService gs;

    @ApiOperation("做什么功能")
    @ApiImplicitParam(name = "参数名称",value = "参数值",dataType = "String")
    @RequestMapping(value = "test",method = RequestMethod.PUT)
    public String testHello(){
        return "hello";
    }

    /**
     * 进入测试页面
     * @param model
     * @return
     */
    @GetMapping(value = "tests")
    public ModelAndView stepOne(Model model){
        return new ModelAndView("test","model",model);
    }

    @PostMapping("seckill")
    @ResponseBody
    public String seckill(@RequestParam(value = "code",required = true) String code,@RequestParam(value = "store",required = true) Integer store){

       return gs.updateGoodsStore(code,store);
        }
}
