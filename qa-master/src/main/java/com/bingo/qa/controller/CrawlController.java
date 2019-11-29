package com.bingo.qa.controller;

import com.bingo.qa.model.HostHolder;
import com.bingo.qa.model.User;
import com.bingo.qa.service.AuthUserService;
import com.bingo.qa.service.CrawlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 爬虫controller
 *
 */

@Controller
@RequestMapping("/crawl")
public class CrawlController {

    private final CrawlService crawlService;

    private final AuthUserService authUserService;

    private final HostHolder hostHolder;

    @Autowired
    public CrawlController(CrawlService crawlService, AuthUserService authUserService, HostHolder hostHolder) {
        this.crawlService = crawlService;
        this.authUserService = authUserService;
        this.hostHolder = hostHolder;
    }


    @GetMapping
    public String crawl(@RequestParam(value = "type") String type,
                        @RequestParam(value = "pageNum") int pageNum) {

        User user = hostHolder.getUser();

        return "redirect:/index";
    }

    /**
     */
    @SpringBootApplication
    @EnableScheduling
    @EnableAsync
    public static class QaApplication {

        public static void main(String[] args) {
            SpringApplication.run(QaApplication.class, args);
        }

    }
}