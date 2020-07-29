package com.wq.elasticsearchapi;

import com.alibaba.fastjson.JSON;
import com.wq.elasticsearchapi.config.ElasticSearchConfig;
import com.wq.elasticsearchapi.pojo.User;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class ElasticsearchApiApplicationTests {

	@Autowired
	@Qualifier("restHighLevelClient")
	private RestHighLevelClient client;

	/**
	 * 创建index(数据库)
	 */
	@Test
	void contextLoads() throws IOException {
		System.setProperty("es.set.netty.runtime.available.processors", "false");
		CreateIndexRequest indexRequest = new CreateIndexRequest("jd_goods");
		CreateIndexResponse response = client.indices().create(indexRequest, RequestOptions.DEFAULT);
		System.out.println(response.toString());
	}

	/**
	 * 查看索引是否存在
	 */
	@Test
	void testExistsIndex() throws IOException {
		GetIndexRequest request = new GetIndexRequest("wq6");
		boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
		System.out.println(exists);
	}

	/**
	 * 删除index
	 */
	@Test
	void testDeleteIndex() throws IOException {
		DeleteIndexRequest request = new DeleteIndexRequest("wq6");
		AcknowledgedResponse response = client.indices().delete(request, RequestOptions.DEFAULT);
		System.out.println(response.isAcknowledged());
	}

	/**
	 * 创建user对象，放入index里面。
	 */
	@Test
	void createUser() throws IOException {
		User user = new User("张三5", 55);
		IndexRequest indexRequest = new IndexRequest("wq_user")
				.type("ints")
				.source(JSON.toJSONString(user), XContentType.JSON);
		IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);
		System.out.println("response: " + response);
		System.out.println("response: " + response.status());
	}

	@Test
	void testJSON() throws IOException {
		User user = new User("zhangsan", 33);
		Object json = JSON.toJSON(user);
		String jsonString = JSON.toJSONString(user);
		System.out.println(json);
		System.out.println(jsonString);
	}

	/**
	 * 从index里面取出user对象   Get API
	 */
	@Test
	void getUser() throws IOException {
		GetRequest getRequest = new GetRequest("wq_user", "ints", "1");
		GetRequest source = getRequest.storedFields("_source");
		GetResponse documentFields = client.get(source, RequestOptions.DEFAULT);
		System.out.println(documentFields.toString());
	}

	/**
	 * Exists API
	 * The exists API returns true if a document exists, and false otherwise.
	 * 如果存在true文档，则存在API返回，false否则返回。
	 */
	@Test
	void testExitAPI() throws IOException {
		GetRequest getRequest = new GetRequest("wq_user", "ints", "1");
		GetRequest sourceContext = getRequest.fetchSourceContext(new FetchSourceContext(false));
		boolean exists = client.exists(sourceContext, RequestOptions.DEFAULT);
		System.out.println(exists);
	}

	@Test
	void testIndexApi() throws IOException {
		Map<String,Object> jsonMap = new HashMap<>();
		jsonMap.put("user","kimchy");
		jsonMap.put("postDate",new Date());
		jsonMap.put("message","trying out Elasticsearch");
		IndexRequest indexRequest = new IndexRequest("posts","doc","1").source(jsonMap);
		IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);
 	}
}