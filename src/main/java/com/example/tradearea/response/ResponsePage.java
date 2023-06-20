package com.example.tradearea.response;

import org.springframework.data.domain.Page;

import java.util.List;


public class ResponsePage<T> {
    private final List<T> content;
    private final int pageNumber;
    private final int pageSize;
    private final int totalPages;

    public ResponsePage(Page<T> page) {
        content = page.getContent();
        pageNumber = page.getNumber() + 1;
        pageSize = page.getSize();
        totalPages = page.getTotalPages();
    }

    public List<T> getContent() {
        return content;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }
}
