package com.xc.mail.elasticsearch.test;

import com.alibaba.fastjson.JSON;
import com.xc.mail.App;
import com.xc.mail.pojo.User;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.cluster.metadata.AliasMetadata;
import org.elasticsearch.common.document.DocumentField;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import javax.swing.text.Highlighter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class ESTest {

    @Autowired
    @Qualifier("restHighLevelClient")
    private RestHighLevelClient restHighLevelClient;

    /**
     * 新建索引
     */

    @Test
    public void createIndex() throws IOException {
        CreateIndexRequest request = new CreateIndexRequest("test1");
        //request.id("2");	//ID也可使用内部自动生成的 不过希望和数据库统一唯一业务ID
        request.index();
        User user = new User();
        user.setId("2");
        user.setName("李四");
        user.setAge("12");

        request.source(JSON.toJSONString(user), XContentType.JSON);

        restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
    }

    /**
     * 查询索引
     */

    @Test
    public void getIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest("test");
        GetIndexResponse getIndexResponse = restHighLevelClient.indices().get(request, RequestOptions.DEFAULT);
        System.out.println(getIndexResponse);
        Map<String, Settings> settings = getIndexResponse.getSettings();
        int size = settings.size();
        System.out.println(size);
        Map<String, List<AliasMetadata>> map =
                getIndexResponse.getAliases();
    }

    /**
     * 删除索引
     */
    @Test
    public void deleteIndex() throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest("test1");
        AcknowledgedResponse delete = restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
        boolean b = delete.isAcknowledged();
        System.out.println(b);
    }


    /**
     * 创建文档
     */

    @Test
    public void createDocumentIndex() throws IOException {
        User user = new User("2", "李四", "16");
        IndexRequest request = new IndexRequest("test");
        request.id("4");
        request.timeout("1s");
        request.source(JSON.toJSONString(user),XContentType.JSON);
        //客户端发送请求
        IndexResponse index = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        System.out.println(index.getIndex());
        System.out.println(index.status());
    }




    /**
     * 获取文档
     * @throws IOException
     */
    @Test
    public void getDocumentIndex() throws IOException {
        GetRequest getRequest = new GetRequest("test", "4");
        GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        if (getResponse.isExists()) {
            String sourceAsString = getResponse.getSourceAsString();
            System.out.println(sourceAsString);

            Map<String, Object> map = getResponse.getSource();
            System.out.println(map);

        }
    }

    /**
     * 更新文档
     */
    @Test
    public void updateDocumentindex() throws IOException {
        User user = new User("2", "李四吃瓜", "10");
        UpdateRequest updateRequest = new UpdateRequest("test", "4");
        updateRequest.doc(JSON.toJSONString(user),XContentType.JSON);

        UpdateResponse update = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        RestStatus status = update.status();
        System.out.println(status);
    }

    /**
     * 删除文档
     */
    @Test
    public void deleteDocumentIndex() throws IOException {
        DeleteRequest test = new DeleteRequest("test", "gO0vqngB4e83xOTmW-Un");
        DeleteResponse delete = restHighLevelClient.delete(test, RequestOptions.DEFAULT);
        System.out.println(delete.status());
    }

    /**
     * 条件查询
     */

    @Test
    public void searchTest() throws IOException {
        SearchRequest searchRequest = new SearchRequest("test");
        //创建搜索条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //高亮显示
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        //设置需要高亮的字段
        highlightBuilder.field("name")
                // 设置前缀、后缀
                .preTags("<font color='red'>")
                .postTags("</font>");

        searchSourceBuilder.highlighter(highlightBuilder);
        //精确匹配
       // TermQueryBuilder builder = QueryBuilders.termQuery("name", "李四吃瓜");
        //匹配所有
        MatchAllQueryBuilder builder = QueryBuilders.matchAllQuery();
        searchSourceBuilder.query(builder);
        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.MILLISECONDS));
        searchRequest.source(searchSourceBuilder);

        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        RestStatus status = search.status();
        System.out.println(status);
        for (SearchHit hit : search.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
       // System.out.println(JSON.toJSON(search.getHits()));

    }


    /**
     * 分页查询
     */
    /*public Page<User> listPage(Pageable pageable, String coutent) throws Exception{
        //创建检索请求
        SearchRequest searchRequest = new SearchRequest("User");
        //创建搜索构建者
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //设置构建搜索属性
        sourceBuilder.from(pageable.getPageNumber() * pageable.getPageSize()); // 需要注意这里是从多少条开始
        sourceBuilder.size(pageable.getPageSize()); //共返回多少条数据
        sourceBuilder.sort(new FieldSortBuilder("id").order(SortOrder.ASC)); //根据自己的需求排序

        if (!StringUtils.isEmpty(coutent)){
            //自定义组合查询
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            TermQueryBuilder termQuery = QueryBuilders.termQuery(STATUS, STATUS);
            MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("coutent",coutent)
                    .fuzziness(Fuzziness.AUTO); //模糊匹配
            boolQueryBuilder.must(termQuery).must(queryBuilder);
            sourceBuilder.query(boolQueryBuilder);
        }
        //传入构建进行搜索
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        //处理结果
        RestStatus restStatus = searchResponse.status();
        if (restStatus != RestStatus.OK){

        }
        List<User> list = new ArrayList<>();
        SearchHits hits = searchResponse.getHits();
        hits.forEach(item -> list.add(JSON.parseObject(item.getSourceAsString(), User.class)));
        return new PageImpl<>(list, pageable, hits.getTotalHits().value);
    }*/

}
