package com.blockchain.controller;

import com.blockchain.model.Product;
import com.blockchain.model.User;
import com.blockchain.service.ProductService;
import com.blockchain.utils.Authorization;
import com.blockchain.utils.CurrentUser;
import com.blockchain.utils.JSONUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "api/product")
public class ProductController
{
	@Autowired
	private ProductService productService;

	// 添加产品
	@Authorization
	@RequestMapping(value = "insertProduct", method = { RequestMethod.POST })
	public String insertProduct(@CurrentUser User user, @RequestBody String request)
	{
		// 1.检查用户身份
		if (user.db.role != User.Roles.Admin) {
			return JSONUtils.failResponse("只有管理员才能添加产品");
		}

		// 2.创建产品对象
		var req = new JSONObject(request);
		var product = new Product();
		product.db.name = req.getString("name");
		product.db.days = req.getInt("days");
		product.db.rate = req.getBigDecimal("rate");
		product.db.additional = req.has("additional") ? req.getString("additional") : null;

		// 3.添加新产品到数据库
		if (productService.insertProduct(product) == 0) {
			return JSONUtils.failResponse("添加失败");
		}

		// 4.返回成功提示
		return JSONUtils.successResponse(product.db.id);
	}

	// 获取所有产品
	@RequestMapping(value = "getAllProducts", method = { RequestMethod.GET })
	public String getAllProducts()
	{
		return JSONUtils.successResponse(JSONUtils.arrayToJSONs(productService.getAllProducts()));
	}

	// 修改产品信息
	@Authorization
	@RequestMapping(value = "updateProduct", method = { RequestMethod.POST })
	public String updateProduct(@CurrentUser User user, @RequestBody String request)
	{
		// 1.检查用户身份
		if (user.db.role != User.Roles.Admin) {
			return JSONUtils.failResponse("只有管理员才能修改产品");
		}

		// 2.查找要修改的产品
		var req = new JSONObject(request);
		var product = productService.getProductById(req.getLong("id"));
		if (product == null){
			return JSONUtils.failResponse("没有此产品");
		}

		// 3.修改产品信息并返回成功提示
		product.db.name = req.getString("name");
		product.db.days = req.getInt("days");
		product.db.rate = req.getBigDecimal("rate");
		product.db.additional = req.has("additional") ? req.getString("additional") : null;
		productService.updateProductInfo(product);
		return JSONUtils.successResponse();
	}

	// 删除产品
	@Authorization
	@RequestMapping(value = "deleteProduct", method = { RequestMethod.POST })
	public String deleteProduct(@CurrentUser User user, @RequestBody String request)
	{
		// 1.检查用户身份
		if (user.db.role != User.Roles.Admin) {
			return JSONUtils.failResponse("只有管理员才能删除产品");
		}

		// 2.查找要删除的产品
		var req = new JSONObject(request);
		var product = productService.getProductById(req.getLong("id"));
		if (product == null){
			return JSONUtils.failResponse("没有此产品");
		}

		// 3.删除产品并返回成功提示
		productService.deleteProductById(product.db.id);
		return JSONUtils.successResponse();
	}
}
