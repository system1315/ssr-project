package com.kh.demo.domain.product.dao;


import com.kh.demo.domain.entity.Product;
import lombok.extern.slf4j.Slf4j;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

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

  @Test
  @DisplayName("상품조회")
  void findById() {
    Long productId = 41L;
    Optional<Product> optionalProduct = productDAO.findById(productId);
    Product findedProduct = optionalProduct.orElseThrow();// 값이 없으면 예외 발생
    log.info("findedProduct={}", findedProduct);
  }

  @Test
  @DisplayName("상품삭제(단건)")
  void deleteById(){
    Long id = 4L;
    int rows = productDAO.deleteById(id);
    log.info("rows={}",rows);
    Assertions.assertThat(rows).isEqualTo(1);
  }

  @Test
  @DisplayName("상품삭제(여러건)")
  void deleteByIds() {
    List<Long> ids = List.of(2L,3L);
    int rows = productDAO.deleteByIds(ids);
    Assertions.assertThat(rows).isEqualTo(2);
  }

}