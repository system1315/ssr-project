package com.kh.board.dto;

import java.util.List;

public class PageDTO<T> {
  private List<T> content;
  private int currentPage;
  private int totalPages;
  private long totalElements;
  private int size;
  private boolean hasNext;
  private boolean hasPrevious;

  public PageDTO(List<T> content, int currentPage, int totalPages, long totalElements, int size) {
    this.content = content;
    this.currentPage = currentPage;
    this.totalPages = totalPages;
    this.totalElements = totalElements;
    this.size = size;
    this.hasNext = currentPage < totalPages;
    this.hasPrevious = currentPage > 1;
  }

  public List<T> getContent() {
    return content;
  }

  public int getCurrentPage() {
    return currentPage;
  }

  public int getTotalPages() {
    return totalPages;
  }

  public long getTotalElements() {
    return totalElements;
  }

  public int getSize() {
    return size;
  }

  public boolean isHasNext() {
    return hasNext;
  }

  public boolean isHasPrevious() {
    return hasPrevious;
  }

  public int[] getPageNumbers() {
    int start = Math.max(1, currentPage - 2);
    int end = Math.min(totalPages, currentPage + 2);
    int[] pages = new int[end - start + 1];
    for (int i = 0; i < pages.length; i++) {
      pages[i] = start + i;
    }
    return pages;
  }
}