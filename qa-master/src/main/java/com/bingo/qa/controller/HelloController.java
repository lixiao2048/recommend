package com.bingo.qa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
//如果你不想使用模板,请使用ResControllre 这个是不渲染模板的
public class HelloController {
    @RequestMapping(value = "/xixi")
    public String hello(Map<String,Object> map){
        map.put("hello","Hello FreeMarker");
        map.put("message","这是一条信息");
        return "hello";
    }

}