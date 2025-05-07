package com.kh.demo.domain.product.svc;

import com.kh.demo.domain.entity.Product;
import com.kh.demo.domain.product.dao.ProductDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductSVCImpl implements ProductSVC{

  final private ProductDAO productDAO;

//  public ProductSVCImpl(ProductDAO productDAO) {
//    this.productDAO = productDAO;
//  }


  @Override
  public Long save(Product product) {
    return productDAO.save(product);
  }

  @Override
  public List<Product> findAll() {
    return productDAO.findAll();
  }
}
