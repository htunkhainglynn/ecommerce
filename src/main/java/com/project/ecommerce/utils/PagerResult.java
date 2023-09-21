package com.project.ecommerce.utils;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PagerResult<T> {

    private Pager pager;
    private List<T> data;

    public static<T> PagerResult<T> of(Page<T> page) {
        PagerResult<T> result = new PagerResult<T>();

        result.data = page.getContent();

        result.pager = Pager.builder()
                .current(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();

        return result;
    }
}
