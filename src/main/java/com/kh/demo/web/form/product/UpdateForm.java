package com.kh.demo.web.form.product;

import lombok.Data;

@Data
public class UpdateForm {
  private Long productId;
  private String pname;
  private Long quantity;
  private Long price;
}
