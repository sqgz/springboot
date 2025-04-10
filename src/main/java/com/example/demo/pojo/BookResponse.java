package com.example.demo.pojo;

public class BookResponse {
    private java.util.List<Book> content;
    private PageableInfo pageable;

    public BookResponse(java.util.List<Book> content, PageableInfo pageable) {
        this.content = content;
        this.pageable = pageable;
    }

    // Getter 和 Setter 方法
    public java.util.List<Book> getContent() {
        return content;
    }

    public void setContent(java.util.List<Book> content) {
        this.content = content;
    }

    public PageableInfo getPageable() {
        return pageable;
    }

    public void setPageable(PageableInfo pageable) {
        this.pageable = pageable;
    }
}
