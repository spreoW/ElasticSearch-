package com.wq.elasticsearchapi.service;

import com.alibaba.fastjson.JSON;
import com.wq.elasticsearchapi.pojo.Goods;
import com.wq.elasticsearchapi.utils.HtmlParseUtil;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.ShardSearchFailure;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.FuzzyQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author wang.q
 * @date 2020/07/27
 *
 */
@Service
public class ContentsService {

    @Autowired
    private HtmlParseUtil htmlParseUtil;
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 爬取数据插入到ElasticSearch
     * @param keyword 搜索关键字
     * @return
     * @throws Exception
     */
    public Boolean parse(String keyword) throws Exception {
        // 爬取数据
        List<Goods> goodsList = htmlParseUtil.prase(keyword);
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("2m");
        for (int i=0;i<goodsList.size();i++){
            bulkRequest.add(new IndexRequest("jd_goods").type("goods").source(JSON.toJSONString(goodsList.get(i)),XContentType.JSON));
        }
        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        return !bulk.hasFailures();
    }

    /**
     * 根据输入的关键字在ElasticSearch里面搜索，将分页查询的数据通过List<Map<>>返回
     * @param keyword  搜索关键字
     * @param pageNo   当前数
     * @param pageSize 查几个
     * @return
     */
    public List<Map<String,Object>> searchPage(String keyword,int pageNo,int pageSize) throws IOException {

        SearchRequest searchRequest = new SearchRequest("jd_goods");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.from(pageNo);
        searchSourceBuilder.size(pageSize);

        //构建精准查询
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("title", keyword);
        searchSourceBuilder.query(termQueryBuilder);
        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        //执行搜索
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        SearchHit[] hits = searchResponse.getHits().getHits();
        List<Map<String,Object>> searchList = new ArrayList<>();
        for (SearchHit sh : hits){
            searchList.add(sh.getSourceAsMap());
        }
        return searchList;
    }
}
