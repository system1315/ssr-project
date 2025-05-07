package com.kh.demo.domain.product.dao;


import com.kh.demo.domain.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
class ProductDAOImplTest {
//  private static final Logger logger = LoggerFactory.getLogger(ProductDAOImplTest.class);

  @Autowired  // 스프링 컨테이너에 동일 타입의 객체를 찾아와 주입해줌
  ProductDAO productDAO;

  @Test
  void testLog() {
//    System.out.println("hi");logging:
//    logger.info("info");
//    logger.warn("warn");
//    logger.error("error");
//    logger.debug("debug");
//    logger.trace("debug");
    log.info("hi");
    log.warn("warn");
    log.error("error");
  }
  @Test
  @DisplayName("상품목록")
  void findAll(){
    List<Product> list = productDAO.findAll();
//    log.info("상품목록={}", list);
    for (Product product : list) {
      log.info("product={}",product);
    }
  }
  
  @Test
  @DisplayName("상품등록")
  void save(){
    Product product = new Product();
    product.setPname("테스트2");
    product.setQuantity(10L);
    product.setPrice(1000000L);

    Long pid = productDAO.save(product);
    log.info("상품번호={}", pid);
  }
}