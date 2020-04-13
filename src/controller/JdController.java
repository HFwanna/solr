package controller;

import java.util.List;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import pojo.ProductModel;
import service.JdService;

@Controller
public class JdController {
	@Resource(name="jdService")
	private JdService jdService;
	//商品列表
	@RequestMapping("list.action")
	public String list(String queryString,String catalog_name,String price,
			String sort,Model model) throws SolrServerException {
		//通过上面4个条件查询对象商品结果
		List<ProductModel> selectProductModelListByQuery = jdService.selectProductModelListByQuery(queryString, catalog_name, price, sort);
		model.addAttribute("productModels", selectProductModelListByQuery);
		model.addAttribute("queryString", queryString);
		model.addAttribute("catalog_name", catalog_name);
		model.addAttribute("price", price);
		model.addAttribute("sort", sort);
		
		
		return "product_list";
	}
}
