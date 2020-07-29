package com.wq.elasticsearchapi.controller;

import com.wq.elasticsearchapi.service.ContentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author wang.q
 * @date 2020/07/27
 */
@RestController
public class ContentsController {

    @Autowired
    private ContentsService contentsService;

    @GetMapping("/parse/{keyword}")
    public Boolean parse(@PathVariable("keyword") String keyword) throws Exception {
        return contentsService.parse(keyword);
    }

    @GetMapping("/search/{keyword}/{pageNo}/{pageSize}")
    public List<Map<String,Object>> searchPage(@PathVariable("keyword") String keyword,
                                               @PathVariable("pageNo")int pageNo,
                                               @PathVariable("pageSize")int pageSize) throws IOException {
        return contentsService.searchPage(keyword,pageNo,pageSize);
    }
}
