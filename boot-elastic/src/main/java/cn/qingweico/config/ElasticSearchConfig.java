package cn.qingweico.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zqw
 * @date 2020/11/7
 */
@Configuration
public class ElasticSearchConfig {
    @Bean
    public RestHighLevelClient restHighLevelClient() {
        //  <!-- elasticsearch-7.9.3 -->
        // org.elasticsearch.client:elasticsearch-res-high-level-client:7.9.3
        // https://www.elastic.co/cn/downloads/past-releases/elasticsearch-7-9-3
        // es 客户端工具 es-head
        // https://github.com/mobz/elasticsearch-head.git
        // kibana 需要和es保持一致的版本
        // https://www.elastic.co/cn/downloads/past-releases/kibana-7-9-3
        // ik分词器
        // https://github.com/medcl/elasticsearch-analysis-ik/releases/tag/v7.9.3
        // 配置ik ${es.home}/plugins/ik/config
        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("127.0.0.1", 9200, "http")
                ));
    }
}
