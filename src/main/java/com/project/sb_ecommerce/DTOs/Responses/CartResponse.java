package com.project.sb_ecommerce.DTOs.Responses;

// DTO representation of Response object of Category entity. Facilitates paginated results.

import com.project.sb_ecommerce.DTOs.Requests.CartDTO;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartResponse
{
    private List<CartDTO> content;
    private Integer limit;
    private Integer offset;
    private Long totalElements;
    private Integer totalPages;
    private boolean lastpage;
}
