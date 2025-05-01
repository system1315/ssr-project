package com.kh.demo.domain.product.dao;

import com.kh.demo.domain.entity.Product;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductDAOImpl implements ProductDAO{

  NamedParameterJdbcTemplate template;

  public ProductDAOImpl(NamedParameterJdbcTemplate template){
    this.template = template;
  }

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
}
