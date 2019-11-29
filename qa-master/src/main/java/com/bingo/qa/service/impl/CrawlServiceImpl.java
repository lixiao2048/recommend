package com.bingo.qa.service.impl;

import com.bingo.qa.async.EventModel;
import com.bingo.qa.async.EventProducer;
import com.bingo.qa.async.EventType;
import com.bingo.qa.model.Comment;
import com.bingo.qa.model.EntityType;
import com.bingo.qa.model.Question;
import com.bingo.qa.service.CommentService;
import com.bingo.qa.service.CrawlService;
import com.bingo.qa.service.QuestionService;
import com.bingo.qa.service.SensitiveService;
import com.bingo.qa.util.QaUtil;
import com.bingo.qa.util.RequestUtil;
import com.bingo.qa.util.TimeUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 爬虫service
 *
 */

@Service
public class CrawlServiceImpl implements CrawlService {

    private static Logger LOGGER = LoggerFactory.getLogger(CrawlServiceImpl.class);

    private final SensitiveService sensitiveService;

    private final CommentService commentService;

    private final QuestionService questionService;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    public CrawlServiceImpl(SensitiveService sensitiveService, CommentService commentService, QuestionService questionService, EventProducer eventProducer) {
        this.sensitiveService = sensitiveService;
        this.commentService = commentService;
        this.questionService = questionService;
        this.eventProducer = eventProducer;
    }

    /**
     * 异步爬取
     *
     * @param type
     * @param pageNum
     */
    @Override
    @Async
    public void crawl(String type, int pageNum) {
     //用于爬取数据的方法全部删除
    }
}
