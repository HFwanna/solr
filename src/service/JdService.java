package service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.stereotype.Service;

import dao.JdDao;
import pojo.ProductModel;

@Service
public class JdService {
	@Resource(name="jdDao")
	private JdDao jdDao;
	public List<ProductModel> selectProductModelListByQuery(
			String queryString,String catalog_name,String price,
			String sort) throws SolrServerException{
			
			return jdDao.selectProductModelListByQuery(queryString, catalog_name, price, sort);
	}
}
