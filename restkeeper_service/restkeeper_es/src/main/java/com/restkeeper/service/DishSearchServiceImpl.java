package com.restkeeper.service;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLQueryExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.sql.parser.SQLExprParser;
import com.alibaba.druid.sql.parser.Token;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.restkeeper.entity.DishEs;
import com.restkeeper.entity.SearchResult;
import com.restkeeper.exception.BussinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.nlpcn.es4sql.domain.Where;
import org.nlpcn.es4sql.exception.SqlParseException;
import org.nlpcn.es4sql.parse.ElasticSqlExprParser;
import org.nlpcn.es4sql.parse.SqlParser;
import org.nlpcn.es4sql.parse.WhereParser;
import org.nlpcn.es4sql.query.maker.QueryMaker;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Service(version = "1.0.0",protocol = "dubbo")
public class DishSearchServiceImpl implements IDishSearchService{
    @Value("${es.host}")
    private String host;

    @Value("${es.port}")
    private int port;


    @Override
    public SearchResult<DishEs> searchAllByCode(String code, int type, int pageNumber, int pageSize) {
        String shopId = RpcContext.getContext().getAttachment("shopId");
        if(StringUtils.isEmpty(shopId)){
            throw new BussinessException("商户号不存在");
        }
        String storeId= RpcContext.getContext().getAttachment("storeId");
        if(StringUtils.isEmpty(storeId)){
            throw new BussinessException("门店不存在");
        }

        String conditon = "code like '%" +code +"' and type = '" + type + "' and is_deleted = 0 and shop_id = '"+shopId+
                "' and store_id = '"+storeId+"' order by last_update_time desc";
        return this.queryIndexContent("dish",conditon,pageNumber,pageSize);
    }



    @Override
    public SearchResult<DishEs> searchDishByCode(String code, int pageNumber, int pageSize) {
        String shopId = RpcContext.getContext().getAttachment("shopId");
        if(StringUtils.isEmpty(shopId)){
            throw new BussinessException("商户号不存在");
        }
        String storeId= RpcContext.getContext().getAttachment("storeId");
        if(StringUtils.isEmpty(storeId)){
            throw new BussinessException("门店不存在");
        }
        String condition = "code like '&" + code + "'% and is_deleted = 0 and shop_id = '"+shopId+
                "' and store_id = '"+storeId+"' order by last_update_time desc";
        return this.queryIndexContent("dish",condition,pageNumber,pageSize);
    }

    @Override
    public SearchResult<DishEs> searchDishByName(String name, int type, int pageNumber, int pageSize) {
        String shopId = RpcContext.getContext().getAttachment("shopId");
        if(StringUtils.isEmpty(shopId)){
            throw new BussinessException("商户号不存在");
        }
        String storeId= RpcContext.getContext().getAttachment("storeId");
        if(StringUtils.isEmpty(storeId)){
            throw new BussinessException("门店不存在");
        }
        String conditon = "dish_name like '%" +name +"' and type = '" + type + "' and is_deleted = 0 and shop_id = '"+shopId+
                "' and store_id = '"+storeId+"' order by last_update_time desc";
        return this.queryIndexContent("dish",conditon,pageNumber,pageSize);
    }

    /**
     * es 中查询数据
     * @param indexName
     * @param condition
     * @param pageNumber
     * @param pageSize
     * @return
     */
    private SearchResult<DishEs> queryIndexContent(String indexName, String condition, int pageNumber, int pageSize) {

        //构建查询
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(RestClient.builder(new HttpHost(host,port,"http")));
        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // 设置分页
        int start = (pageNumber - 1)*pageSize;
        sourceBuilder.from(start);
        sourceBuilder.size(pageSize);
        // 是否跟踪查询的总命中数
        sourceBuilder.trackTotalHits(true);
        // 设置查询条件
        BoolQueryBuilder boolQueryBuilder = this.createQueryBuilder(indexName,condition);
        sourceBuilder.query(boolQueryBuilder);
        searchRequest.source(sourceBuilder);

        // 获取查询结果
        SearchResponse searchResponse = null;
        try {
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        }catch (IOException e){
            log.error(e.getMessage());
            e.printStackTrace();
        }
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        // 封装返回结果
        List<DishEs> listData = Lists.newArrayList();
        for (SearchHit hit: searchHits) {
            Map<String,Object> datas = hit.getSourceAsMap();
            String jsonMap= JSON.toJSONString(datas);
            DishEs dish = JSON.parseObject(jsonMap,DishEs.class);
            listData.add(dish);
        }

        //关闭客户端连接
        try {
            restHighLevelClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        SearchResult<DishEs> result = new SearchResult<>();
        result.setRecords(listData);
        result.setTotal(searchResponse.getHits().getTotalHits().value);

        return result;
    }

    /**
     * 构建查询条件
     * @param indexName
     * @param condition
     * @return
     */
    private BoolQueryBuilder createQueryBuilder(String indexName, String condition) {
        BoolQueryBuilder boolQueryBuilder = null;
        try {

            SqlParser sqlParser = new SqlParser();
            String sql = "select * from "+indexName;
            String whereTemp = "";
            if(StringUtils.isNotEmpty(condition)){
                whereTemp = "where 1=1 and " + condition;
            }

            SQLQueryExpr sqlExpr = (SQLQueryExpr) toSqlExpr(sql+whereTemp);
            MySqlSelectQueryBlock query = (MySqlSelectQueryBlock) sqlExpr.getSubQuery().getQuery();
            WhereParser whereParser = new WhereParser(sqlParser,query);
            Where where = whereParser.findWhere();
            if(where != null){
                boolQueryBuilder = QueryMaker.explan(where);
            }
        }catch (SqlParseException e){
            log.error("ReadES.createQueryBuilderByExpress-Exception,"+e.getMessage());
        }
        return boolQueryBuilder;

    }

    private SQLExpr toSqlExpr(String sql){
        SQLExprParser parser = new ElasticSqlExprParser(sql);
        SQLExpr expr = parser.expr();
        if(parser.getLexer().token()!= Token.EOF){
            throw new ParserException("illegal sql expr : " + sql);
        }
        return expr;
    }
}
