package cn.qingweico.service;

import cn.qingweico.entity.Content;
import cn.qingweico.util.HtmlParseUtil;
import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zqw
 * @date 2020/11/8
 */
@Service
public class ContentService {
    HtmlParseUtil htmlParseUtil;

    RestHighLevelClient restHighLevelClient;

    @Autowired
    public void setRestHighLevelClient(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    @Autowired
    public void setHtmlParseUtil(HtmlParseUtil htmlParseUtil) {
        this.htmlParseUtil = htmlParseUtil;
    }

    /**
     * 将爬取的数据写入到elastic中
     *
     * @param keyword 关键词
     * @return 是否写入数据
     * @throws IOException IOException
     */
    public boolean hasSucceedParseContent(String keyword) throws IOException {
        List<Content> list = htmlParseUtil.getContentList(keyword);
        BulkRequest request = new BulkRequest();
        for (Content content : list) {
            request.add(new IndexRequest("content")
                    .source(JSON.toJSONString(content), XContentType.JSON));
        }
        BulkResponse bulk = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        return !bulk.hasFailures();
    }

    /**
     * @param keyword   关键词
     * @param pageIndex 分页起始页
     * @param pageSize  分页的每页数量
     * @return List
     * @throws IOException IOException
     */
    public List<Map<String, Object>> searchPage(String keyword, int pageIndex, int pageSize) throws IOException {
        if (pageIndex <= 1) {
            pageIndex = 1;
        }
        // 条件搜索
        SearchRequest request = new SearchRequest("content");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        // 分页
        searchSourceBuilder.from(pageIndex);
        searchSourceBuilder.size(pageSize);

        // 精准匹配 按名称搜索
        TermQueryBuilder termQueryBuilder = new TermQueryBuilder("name", keyword);
        searchSourceBuilder.query(termQueryBuilder);

        // 高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.requireFieldMatch(false);
        highlightBuilder.field("name");
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.postTags("</span>");
        searchSourceBuilder.highlighter(highlightBuilder);

        // 执行搜索
        request.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(request, RequestOptions.DEFAULT);

        // 解析结果
        List<Map<String, Object>> list = new ArrayList<>();
        for (SearchHit searchHit : searchResponse.getHits().getHits()) {
            Map<String, HighlightField> highFields = searchHit.getHighlightFields();
            HighlightField name = highFields.get("name");
            Map<String, Object> map = searchHit.getSourceAsMap();
            if (name != null) {
                Text[] fragment = name.fragments();
                StringBuilder sb = new StringBuilder();
                for (Text text : fragment) {
                    sb.append(text);
                }
                map.put("name", sb.toString());
            }

            list.add(searchHit.getSourceAsMap());
        }
        return list;
    }
}
