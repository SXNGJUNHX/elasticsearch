package com.test.book.repository;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Repository;

@Repository
public class ElasticsearchRepository {

    private RestHighLevelClient client;

    public ElasticsearchRepository() {
        this.client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );
    }

}
