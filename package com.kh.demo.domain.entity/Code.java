package com.kh.demo.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor  // default 생성자 자동생성
public class Code {
  private String codeId;    //코드
  private String decode;  //디코드
} 