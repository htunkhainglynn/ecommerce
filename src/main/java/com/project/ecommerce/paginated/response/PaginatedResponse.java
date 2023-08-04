package com.project.ecommerce.paginated.response;

import lombok.Data;

import java.util.List;

@Data
public class PaginatedResponse<T> {
    private List<T> items;
    private long totalElements;
    private int totalPages;

}

