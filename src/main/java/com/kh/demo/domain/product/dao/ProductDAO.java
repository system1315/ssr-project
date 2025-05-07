package com.kh.demo.domain.product.dao;

import com.kh.demo.domain.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductDAO {
  //상품등록
  Long save(Product product);

  //상품목록
  List<Product> findAll();

  //상품조회
  Optional<Product> findById(Long id);
}
