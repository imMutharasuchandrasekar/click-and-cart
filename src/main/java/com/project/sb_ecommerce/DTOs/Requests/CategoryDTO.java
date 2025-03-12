package com.project.sb_ecommerce.DTOs.Requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO representation of Category entity.  Meant to be passed as payload.


@NoArgsConstructor
@AllArgsConstructor
@Data
public class CategoryDTO {

    private Long categoryId;

    private String categoryName;
}
