package com.kh.demo.domain.product.svc;

import com.kh.demo.domain.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductSVC {
  // 등록
  Long save(Product product);  
  //상품목록
  List<Product> findAll();

  // 상품조회
  Optional<Product> findById(Long id);

  //상품삭제(단건)
  int deleteById(Long id);

  //상품삭제(여러건)
  int deleteByIds(List<Long> ids);
}
