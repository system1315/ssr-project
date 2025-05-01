package com.kh.demo.domain.entity;

import lombok.*;

//@Getter  : getter 메소드 자동생성
//@Setter  : setter 메소드 자동생성 
//@NoArgsConstructor : 디폴트 생성자 자동생성
//@RequiredArgsConstructor : final 필드를 매개변수로 하는 생성자 자동 생성
//@AllArgsConstructor : 모든 멤버 필드를 매개변수로 하는 생성자 자동 생성
//@ToString : toString 메소드 재정의 자동생성
//@EqualsAndHashCode : equals, hashcode 메소드 자송 생성
@Data // @Getter + @Setter + @ToString + @EqualsAndHashCode + @RequiredArgsConstructor
public class Product {
  private Long productId;   //상품아이디
  private String pname;     //상품명
  private Long quantity;    //상품수량
  private Long price;       //상품가격
}
