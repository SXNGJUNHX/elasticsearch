package com.test.book.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.book.dto.BookDTO;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class ElasticsearchRepository {

    private RestHighLevelClient client;

    public ElasticsearchRepository() {
        this.client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );
    }

    public List<Map<String, Object>> search(String word) {

        try {

            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

            SearchRequest searchRequest = new SearchRequest("book");

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().size(100);

            searchSourceBuilder.query(QueryBuilders.boolQuery()
                    .must(QueryBuilders.matchQuery("title", word))
                    .should(QueryBuilders.matchPhraseQuery("title", word))
            );

            searchRequest.source(searchSourceBuilder);

            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            SearchHits searchHits = searchResponse.getHits();

            for (SearchHit hit : searchHits) {
                Map<String,Object> map = hit.getSourceAsMap();
                map.put("id", hit.getId()); //id == map.get("seq")
                map.put("score", hit.getScore());
                list.add(map);
            }

            //client.close(); //실수;;

            return list;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    public void addDocument(BookDTO dto) {

        try {
            //입력 도서 > Elasticsearch index
            ObjectMapper om = new ObjectMapper();

            String data = om.writeValueAsString(dto);
            //System.out.println(data);

            IndexRequest request = new IndexRequest("book")
                    .source(data, XContentType.JSON)
                    .setRefreshPolicy("wait_for")
                    ;

            request.id(dto.getSeq().toString());

            client.index(request, RequestOptions.DEFAULT); //문서 추가

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}























