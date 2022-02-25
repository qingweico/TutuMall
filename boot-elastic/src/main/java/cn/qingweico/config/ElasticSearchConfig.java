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
        // ik ~/plugins/ik/config
        //  <!-- elasticsearch-7.9.3 -->
        // org.elasticsearch.client:elasticsearch-res-high-level-client:7.9.3
        // https://www.elastic.co/cn/downloads/past-releases/elasticsearch-7-9-3
        // https://github.com/mobz/elasticsearch-head.git
        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("127.0.0.1", 9200, "http")
                ));
    }
}
