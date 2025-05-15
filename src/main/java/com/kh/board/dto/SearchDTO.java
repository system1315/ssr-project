package com.kh.board.dto;

public class SearchDTO {
  private String searchType;
  private String keyword;
  private int page;
  private int size;

  public SearchDTO() {
    this.page = 1;
    this.size = 10;
  }

  public String getSearchType() {
    return searchType;
  }

  public void setSearchType(String searchType) {
    this.searchType = searchType;
  }

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public int getPage() {
    return page;
  }

  public void setPage(int page) {
    this.page = page;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public int getOffset() {
    return (page - 1) * size;
  }
}