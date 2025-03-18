package com.project.sb_ecommerce.DTOs.Responses;

import com.project.sb_ecommerce.DTOs.Requests.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaginatedProductResponse
{
    List<ProductDTO> contents;
    private Integer limit;
    private Integer offset;
    private Long totalElements;
    private Integer totalPages;
    private boolean lastpage;
}
