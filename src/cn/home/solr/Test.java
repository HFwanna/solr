package cn.home.solr;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.SolrParams;

public class Test {
	@org.junit.Test
	public void testAddDocument() throws SolrServerException, IOException {
		String baseUrl = "http://localhost:8080/solr/";
		//HttpSolrServer是SolrServer的子类（单机版） 还有ColorSolrServer(集群版)
		SolrServer solrServer = new HttpSolrServer(baseUrl);
		
		SolrInputDocument document = new SolrInputDocument();
		document.setField("id", "teset");
		document.setField("product_name", "冰箱");
		//1000ms自动提交
		solrServer.add(document, 1000);
		
	}
	
	@org.junit.Test
	public void testDeleteDocument() throws SolrServerException, IOException {
		String baseUrl = "http://localhost:8080/solr/";
		//HttpSolrServer是SolrServer的子类（单机版） 还有ColorSolrServer(集群版)
		SolrServer solrServer = new HttpSolrServer(baseUrl);
		
		solrServer.deleteById("teset");
		solrServer.commit();
	}
	
	//更新
	@org.junit.Test
	public void testUpdate() throws SolrServerException, IOException {
		String baseUrl = "http://localhost:8080/solr/";
		//HttpSolrServer是SolrServer的子类（单机版） 还有ColorSolrServer(集群版)
		SolrServer solrServer = new HttpSolrServer(baseUrl);
		
		SolrInputDocument doc = new SolrInputDocument();
		doc.setField("id", "teset");
		doc.setField("product_name", "update冰箱");
		//更新和添加用同样一个方法add,只要id相同就是更新，不同就是增加
		solrServer.add(doc);
		solrServer.commit();
	}
	
	//查询
	@org.junit.Test
	public void testQuery() throws SolrServerException, IOException {
		String baseUrl = "http://localhost:8080/solr/";
		//HttpSolrServer是SolrServer的子类（单机版） 还有ColorSolrServer(集群版)
		SolrServer solrServer = new HttpSolrServer(baseUrl);
		
		//查询关键词冰箱 
		SolrQuery solrQuery = new SolrQuery();//SolrParams 子类
		//添加查询条件  solrQuery.set("q", "product_name:冰箱") == solrQuery.setQuery("product_name:冰箱")
		solrQuery.set("q", "product_name:冰箱");
		//设定默认域，比如设定这个以后，上面的 product_name:冰箱   可以变成    冰箱    即可  
		solrQuery.set("df", "product_name");
		//过滤条件 *表示无穷小
		solrQuery.addFilterQuery("product_price:[0 TO 3]");  
		solrQuery.addFilterQuery("product_name:家天下");  
		//注意不能使用如下，会覆盖掉前一个fq，所以多个条件应该使用上面方法
//		solrQuery.set("fq", "product_price:[0 TO 3]");
//		solrQuery.set("fq", "product_name:家天下");
		//价格排序
		solrQuery.addSort("product_price",ORDER.desc);
		//分页 查询结果的序列2的数据+显示两条数据
		solrQuery.setStart(2);
		solrQuery.setRows(2);
		//只查询指定域
		solrQuery.set("fl", "id,product_name");
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
		//取出文档域数据
		for (SolrDocument solrDocument : results) {
			System.out.println(solrDocument.get("id"));
			System.out.println(solrDocument.get("product_price"));
			System.out.println(solrDocument.get("product_name"));
			Map<String, List<String>> map = highlighting.get(solrDocument.get("id"));
			//输出高亮域内容
			List<String> list = map.get("product_name");
			System.out.println(list.get(0));;
		}
		solrServer.commit();
	}
	
}
