package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.jdbc.Null;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.stereotype.Repository;

import pojo.ProductModel;


@Repository(value="jdDao")
public class JdDao {
	@Resource
	private SolrServer solrServer;
	
	//通过上面4个条件查询对象商品结果集
	public List<ProductModel> selectProductModelListByQuery(
			String queryString,String catalog_name,String price,
			String sort) throws SolrServerException{
		//查询关键词冰箱 
		SolrQuery solrQuery = new SolrQuery();//SolrParams 子类
		//添加查询条件  solrQuery.set("q", "product_name:冰箱") == solrQuery.setQuery("product_name:冰箱")
		solrQuery.set("q", "product_name:" + queryString);
		//设定默认域，比如设定这个以后，上面的 product_name:冰箱   可以变成    冰箱    即可  
		solrQuery.set("df", "product_name");
		if (null != catalog_name && !"".equals(catalog_name)) {
			solrQuery.addFilterQuery("product_catalog_name:" + catalog_name);
			
		}
		if (null != price && !"".equals(price)) {
			String[] p = price.split("-");
			solrQuery.addFilterQuery("product_price:[" + p[0] + " TO " + p[1] + "]");
		} 
		//价格排序
		if ("1".equals(sort)) {
			solrQuery.addSort("product_price",ORDER.desc);
		}else {
			solrQuery.addSort("product_price",ORDER.asc);
		}
		//分页 查询结果的序列2的数据+显示两条数据
		solrQuery.setStart(0);
		solrQuery.setRows(16);
		//只查询指定域
		solrQuery.set("fl", "id,product_name,product_price,product_picture");
		//高亮
		//打开高亮开关
		solrQuery.setHighlight(true);
		//指定高亮域  这个前后缀会把查询q的内容 "冰箱" 包围
		solrQuery.setHighlightSimplePre("<span style=\"color:red\">");
		solrQuery.setHighlightSimplePost("</span>");
		
		
		//执行查询
		QueryResponse response = solrServer.query(solrQuery);
		//文档结果集
		SolrDocumentList results = response.getResults();
		//总条数
		long numFound = results.getNumFound();
		System.out.println("总条数：" + numFound);
		
		//因为highlight的查询结果另存在另外的容器中，所以要单独提取出阿里
		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
		
		List<ProductModel> productModels = new ArrayList<ProductModel>();
		//取出文档域数据
		for (SolrDocument solrDocument : results) {
			ProductModel productModel = new ProductModel();
			productModel.setPid((String)solrDocument.get("id"));
			productModel.setPicture((String)solrDocument.get("product_picture"));
			if (null != solrDocument.get("product_price")) {
				productModel.setPrice((float)solrDocument.get("product_price"));
			}
			
			
			Map<String, List<String>> map = highlighting.get(solrDocument.get("id"));
			//输出高亮域内容
			List<String> list = map.get("product_name");
//			System.out.println(list.get(0));
			productModel.setName((String)list.get(0));
			productModels.add(productModel);
			
		}
		
		return productModels;
		
	}
}
