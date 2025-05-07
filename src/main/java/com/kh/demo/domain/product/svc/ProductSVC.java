package com.kh.demo.domain.product.svc;

import com.kh.demo.domain.entity.Product;

import java.util.List;

public interface ProductSVC {
  // 등록
  Long save(Product product);  
  //상품목록
  List<Product> findAll();
}
