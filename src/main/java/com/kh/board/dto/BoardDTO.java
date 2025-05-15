package com.kh.board.dto;

import java.time.LocalDateTime;

public class BoardDTO {
  private Long id;
  private String title;
  private String content;
  private String author;
  private LocalDateTime createdDate;
  private LocalDateTime modifiedDate;

  public BoardDTO() {
  }

  public BoardDTO(String title, String content, String author) {
    this.title = title;
    this.content = content;
    this.author = author;
    this.createdDate = LocalDateTime.now();
    this.modifiedDate = LocalDateTime.now();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public LocalDateTime getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(LocalDateTime createdDate) {
    this.createdDate = createdDate;
  }

  public LocalDateTime getModifiedDate() {
    return modifiedDate;
  }

  public void setModifiedDate(LocalDateTime modifiedDate) {
    this.modifiedDate = modifiedDate;
  }

  @Override
  public String toString() {
    return "BoardDTO{" +
        "id=" + id +
        ", title='" + title + '\'' +
        ", content='" + content + '\'' +
        ", author='" + author + '\'' +
        ", createdDate=" + createdDate +
        ", modifiedDate=" + modifiedDate +
        '}';
  }
}