package controller;

import annotation.MyAutowired;
import annotation.MyService;
import annotations.MyRequestMapping;
import annotations.MySecurity;
import service.IMySimpleSpringService;

/**
 * @author dingpei
 * @Description: todo
 * @date 2020/12/11 9:57 上午
 */
@MyService
@MyRequestMapping("")
public class TestController {
    @MyAutowired
    private IMySimpleSpringService mySimpleSpringServiceJdk;

    @MyRequestMapping("/")
    @MySecurity({"zhangsan","wangwu"})
    public String testController(String name){
        return mySimpleSpringServiceJdk.test().toString();
    }
}
