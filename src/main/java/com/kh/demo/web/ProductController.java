package com.kh.demo.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/products")
public class ProductController {
  //목록
  @GetMapping       // GET  http://localhost:9080/products
  public String findAll() {

    return "product/all";   //veiw
  }

  //
  @ResponseBody
  @GetMapping("/test1")   // GET http://localhost:9080/products/test1
  public String test1() {
    return "test1";
  }

}
