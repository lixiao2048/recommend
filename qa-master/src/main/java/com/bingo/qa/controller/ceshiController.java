package com.bingo.qa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
@Controller
public class ceshiController {
        @RequestMapping(value = "/1434")
        public String hello(Model model) {

            List<String> colors = Arrays.asList(new String[]{"red","blue","black"});
            model.addAttribute("colors",colors);
            return "测试";
        }
    }
