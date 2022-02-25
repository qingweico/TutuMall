package cn.qingweico;

import cn.qingweico.entity.User;
import cn.qingweico.service.ContentService;
import cn.qingweico.util.HtmlParseUtil;
import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class BootElasticApplicationTests {
    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Autowired
    HtmlParseUtil htmlParseUtil;

    @Autowired
    ContentService contentService;

    @Autowired
    User user;

    //创建索引
    @Test
    void contextLoads() throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("user");
        CreateIndexResponse response = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        System.out.println(response);

    }

    //查找索引
    @Test
    void getIndexRequestTest() throws IOException {
        GetIndexRequest getIndexRequest = new GetIndexRequest("qiming");
        boolean exists = restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    //删除索引
    @Test
    void deleteIndexRequestTest() throws IOException {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("qiming");
        AcknowledgedResponse delete = restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        System.out.println(delete.isAcknowledged());
    }

    //添加文档
    @Test
    void addDocumentTest() throws IOException {
        user.setName("user01");
        user.setPassword("01");
        IndexRequest request = new IndexRequest("user");
        request.id("1");
        request.timeout("1s");
        request.source(JSON.toJSON(user), XContentType.JSON);
        IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        System.out.println(response.status());
        System.out.println(response);
    }

    //判断文档是否存在
    @Test
    void isExistsTest() throws IOException {
        GetRequest request = new GetRequest("user", "_doc", "1");
        request.fetchSourceContext(new FetchSourceContext(false));
        request.storedFields("_none_");
        boolean isExists = restHighLevelClient.exists(request, RequestOptions.DEFAULT);
        System.out.println(isExists);
    }

    //获取文档
    @Test
    void getDocumentTest() throws IOException {
        GetRequest request = new GetRequest("user", "_doc", "1");
        GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
        System.out.println(response.getSourceAsString());
    }

    //更新文档
    @Test
    void updateDocumentTest() throws IOException {
        UpdateRequest request = new UpdateRequest("user", "1");
        user.setName("user02");
        user.setPassword("02");
        request.doc(JSON.toJSON(user), XContentType.JSON);
        UpdateResponse response = restHighLevelClient.update(request, RequestOptions.DEFAULT);
        System.out.println(response);
        System.out.println(response.status());
    }

    //删除文档
    @Test
    void deleteDocumentTest() throws IOException {
        DeleteRequest request = new DeleteRequest("user", "1");
        DeleteResponse response = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
        System.out.println(response.status());
    }

    //批量插入数据
    @Test
    void batchTest() throws IOException {
        BulkRequest request = new BulkRequest();
        for (int i = 0; i < 10; i++) {
            user.setName("user" + (i + 1));
            user.setPassword(i + 1 + "");
            request.add(new IndexRequest("user")
                    .id("" + (i + 1))
                    .source(JSON.toJSONString(user), XContentType.JSON));
        }
        BulkResponse responses = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        System.out.println(responses.hasFailures());

    }

    @Test
    void name() throws IOException {
        System.out.println(contentService.hasSucceedParseContent("java"));
    }

}
