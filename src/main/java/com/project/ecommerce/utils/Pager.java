package com.project.ecommerce.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Pager {

    private int current;

    private int size;

    private long totalElements;

    private int totalPages;
}
