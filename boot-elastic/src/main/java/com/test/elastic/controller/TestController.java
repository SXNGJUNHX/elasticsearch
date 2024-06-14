package com.test.elastic.controller;

import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TestController {

    @GetMapping(value = "/list")
    public String list(Model model, String type, String word, @RequestParam(defaultValue = "0") int slop, String word2) {

        if(type == null || type.equals("")){
            type = "match_all";
        }

        try {

            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

            RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));

            SearchRequest searchRequest = new SearchRequest("spring");

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().size(100);

            // 질의 종류
            if(type.equals("match_all")){
                //전문 검색
                searchSourceBuilder.query(QueryBuilders.matchAllQuery());
            } else if(type.equals("match_or")){
                //match 검색(or)
                searchSourceBuilder.query(QueryBuilders.matchQuery("message", word));
            } else if(type.equals("match_and")){
                //match 검색(and)
                searchSourceBuilder.query(QueryBuilders.matchQuery("message", word).operator(Operator.AND));
            } else if(type.equals("match_phrase")){
                //구 검색
                if(slop == 0){
                    searchSourceBuilder.query(QueryBuilders.matchPhraseQuery("message", word));
                } else {
                    searchSourceBuilder.query(QueryBuilders.matchPhraseQuery("message", word).slop(slop));
                }
            } else if(type.equals("match_bool")){
                //boolean 검색(must + must_not)
                searchSourceBuilder.query(QueryBuilders.boolQuery()
                        .must(QueryBuilders.matchQuery("message", word))
                        .mustNot(QueryBuilders.matchQuery("message", word2))
                );
            } else if(type.equals("match_bool_should")){
                //boolean 검색(should)
                searchSourceBuilder.query(QueryBuilders.boolQuery()
                        .must(QueryBuilders.matchQuery("message", word))
                        //.should(QueryBuilders.matchPhraseQuery("message", word))
                        .should(QueryBuilders.matchQuery("message", "검은색"))
                );
            } else if(type.equals("match_bool_keyword")){
                searchSourceBuilder.query(QueryBuilders.boolQuery()
                        .filter(QueryBuilders.matchQuery("message.keyword", word))
                );
            }


            searchRequest.source(searchSourceBuilder); // 요청 준비 완료

            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            SearchHits searchHits = searchResponse.getHits(); //검색 결과

            for(SearchHit hit : searchHits.getHits()) {

                //hit > Documnet
                //Map<String, Object> map = new HashMap<String, Object>();

                Map<String, Object> map = hit.getSourceAsMap();
                map.put("id", hit.getId());
                map.put("score", hit.getScore());

                //System.out.println(hit.getId());
                //System.out.println(hit.getSourceAsMap().get("message"));

                list.add(map);
            }

            model.addAttribute("list", list);

            client.close();


        } catch (Exception e){
            e.printStackTrace();
        }

        model.addAttribute("type", type);
        model.addAttribute("word", word);

        return "list";
    }

    @GetMapping(value = "/add")
    public String add() {
        return "add";
    }

    @PostMapping(value = "/addok")
    public String addok(String id, String message) {

        try {

            //자바 > 엘라스틱 서치
            RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));

            /*

            PUT spring/_doc/1
            {
                "message": "입력받는 문장"
            }

            */

            String data = String.format("{ \"message\": \"%s\" }", message);

            IndexRequest request = new IndexRequest("spring")
                    .source(data, XContentType.JSON)
                    .setRefreshPolicy("wait_for")
                    ;

            request.id(id); // 문서 아이디

            IndexResponse response = client.index(request, RequestOptions.DEFAULT);

            client.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/list";
    }

}
