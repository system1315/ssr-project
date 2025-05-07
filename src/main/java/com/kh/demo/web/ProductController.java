package com.kh.demo.web;

import co.elastic.clients.elasticsearch._types.analysis.NoriAnalyzer;
import com.kh.demo.domain.entity.Product;
import com.kh.demo.domain.product.svc.ProductSVC;
import com.kh.demo.web.form.product.SaveForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

  final private ProductSVC productSVC;

  //목록
  @GetMapping       // GET  http://localhost:9080/products
  public String findAll(Model model) {
    List<Product> list = productSVC.findAll();
    model.addAttribute("list", list);
    return "product/all";   //view
  }

  //상품등록화면
  @GetMapping("/add")       // GET  http://localhost:9080/products/add
  public String addForm(){

    return "product/add";  //view
  }


  //상품등록처리
  @PostMapping("/add")      // POST http://localhost:9080/products/add
  public String add(
      //case1)
//      @RequestParam("pname") String pname,
//      @RequestParam("price") Long price,
//      @RequestParam("quantity") Long quantity
      SaveForm saveForm

  ){
//      log.info("pname={},price={},quantity={}",pname,price,quantity);
    log.info("pname={},price={},quantity={}",saveForm.getPname(),saveForm.getPrice(),saveForm.getQuantity());

    Product product = new Product();
    product.setPname(saveForm.getPname());
    product.setQuantity(saveForm.getQuantity());
    product.setPrice(saveForm.getPrice());

    Long pid = productSVC.save(product);

    return null;
//    return "redirect:/products/{id}";
  }

  //
  @ResponseBody
  @GetMapping("/test1")   // GET http://localhost:9080/products/test1
  public String test1() {
    return "test1";
  }

}
