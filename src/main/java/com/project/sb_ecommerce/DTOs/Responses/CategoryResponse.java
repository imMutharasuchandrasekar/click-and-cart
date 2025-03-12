package com.project.sb_ecommerce.DTOs.Responses;

import com.project.sb_ecommerce.DTOs.Requests.CategoryDTO;
import lombok.*;

import java.util.List;

// DTO representation of Response object of Category entity. Meant to be passed as response.

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class CategoryResponse {

    private List<CategoryDTO> content;
    private Integer limit;
    private Integer offset;
    private Long totalElements;
    private Integer totalPages;
    private boolean lastpage;
}
