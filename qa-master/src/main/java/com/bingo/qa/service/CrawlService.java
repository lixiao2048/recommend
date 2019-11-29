package com.bingo.qa.service;

/**
 *
 */

public interface CrawlService {
    /**
     * 爬虫方法
     * @param type
     * @param pageNum
     */
    void crawl(String type, int pageNum);
}
