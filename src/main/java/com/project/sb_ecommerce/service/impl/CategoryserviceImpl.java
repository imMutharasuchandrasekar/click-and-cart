package com.project.sb_ecommerce.service.impl;

import com.project.sb_ecommerce.DTOs.Requests.CategoryDTO;
import com.project.sb_ecommerce.DTOs.Responses.CategoryResponse;
import com.project.sb_ecommerce.exceptions.APIException;
import com.project.sb_ecommerce.exceptions.ResourceNotFoundException;
import com.project.sb_ecommerce.model.Category;
import com.project.sb_ecommerce.repository.CategoryRepository;
import com.project.sb_ecommerce.service.Categoryservice;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryserviceImpl implements Categoryservice {

    @Autowired
    private CategoryRepository categoryrepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryDTO addCategory( CategoryDTO categorydto )
    {
        Category categorytoBesaved = modelMapper.map( categorydto, Category.class );
        Optional<Category> optionalCategory = categoryrepository.findByCategoryName( categorytoBesaved.getCategoryName() );
        if( optionalCategory.isPresent() )
            throw new APIException( "The category with name " + categorydto.getCategoryName() +" already exists" );

        Category savedCategory = categoryrepository.save( categorytoBesaved );
        return  modelMapper.map( savedCategory, CategoryDTO.class );
    }

    @Override
    public CategoryResponse getAllCategories( Integer offset, Integer limit, String sortBy, String sortOrder )
    {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase( "asc" )
                ? Sort.by( sortBy ).ascending() : Sort.by( sortBy ).descending();

        Pageable pagedRequest = PageRequest.of( offset, limit, sortByAndOrder );
        Page<Category> categoryPage = categoryrepository.findAll( pagedRequest );
        List<Category> allCategories = categoryPage.getContent();
        if( allCategories.isEmpty() )
            throw new APIException( "No categories were present in the database !" );

        List<CategoryDTO> categoryDTOList = allCategories.stream()
                .map( category -> modelMapper.map( category, CategoryDTO.class) ).toList();
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent( categoryDTOList );

        categoryResponse.setOffset( categoryPage.getNumber() );
        categoryResponse.setLimit( categoryPage.getSize() );
        categoryResponse.setTotalElements( categoryPage.getTotalElements() );
        categoryResponse.setTotalPages( categoryPage.getTotalPages() );
        categoryResponse.setLastpage( categoryPage.isLast() );

        return categoryResponse;
    }

    @Override
    public Category updateCategory( Category category, Long categoryId )
    {
        Category tobeUpdated = categoryrepository.findById( categoryId )
                .orElseThrow( () -> new ResourceNotFoundException( "Category", "categoryId", categoryId ) );

        tobeUpdated.setCategoryName( category.getCategoryName() );
        return categoryrepository.save( tobeUpdated );
    }

    @Override
    public String deleteCategory( Long categoryId )
    {
        Category tobeDeleted = categoryrepository.findById( categoryId )
                .orElseThrow( () -> new ResourceNotFoundException( "Category", "categoryId", categoryId ) );

        categoryrepository.delete( tobeDeleted );
        return "Category of id " + categoryId + " is deleted successfully";
    }

}
