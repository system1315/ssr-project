package com.kh.demo.domain.product.dao;

import com.kh.demo.domain.entity.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
class ProductDAOImpl implements ProductDAO{

    final private NamedParameterJdbcTemplate template;

//  //필드 주입
//  @Autowired
//  NamedParameterJdbcTemplate template;

  //생성자 주입
//  public ProductDAOImpl(NamedParameterJdbcTemplate template){
//    this.template = template;
//  }

  
  
  
  RowMapper<Product> productRowMapper(){

    return (rs, rowNum)->{
      Product product = new Product();
      product.setProductId(rs.getLong("product_id"));
      product.setPname(rs.getString("pname"));
      product.setQuantity(rs.getLong("quantity"));
      product.setPrice(rs.getLong("price"));
      return product;
    };
  }

  /**
   * 상품 등록
   * @param product
   * @return 상품번호
   */
  @Override
  public Long save(Product product) {
    StringBuffer sql = new StringBuffer();
    sql.append("INSERT INTO product (product_id,pname,quantity,price) ");
    sql.append("     VALUES (PRODUCT_PRODUCT_ID_SEQ.nextval,:pname,:quantity,:price) ");

    SqlParameterSource param = new BeanPropertySqlParameterSource(product);

    // template.update()가 수행된 레코드의 특정 컬럼값을 읽어오는 용도
    KeyHolder keyHolder = new GeneratedKeyHolder(); 
    long rows = template.update(sql.toString(),param, keyHolder, new String[]{"product_id"} );
    log.info("rows={}",rows);

    Number pidNumber = (Number)keyHolder.getKeys().get("product_id");
    long pid = pidNumber.longValue();
    return pid;
  }

  /**
   * 상품 목록
   * @return 상품 목록
   */
  @Override
  public List<Product> findAll() {
    //sql
    StringBuffer sql = new StringBuffer();
    sql.append("  SELECT product_id, pname, quantity, price ");
    sql.append("    FROM product ");
    sql.append("order BY product_id desc ");

    //db요청
    List<Product> list = template.query(sql.toString(), productRowMapper());

    return list;
  }

  /**
   * 상품조회
   * @param id 상품번호
   * @return 상품정보
   */
  @Override
  public Optional<Product> findById(Long id) {
    return Optional.empty();
  }
}
