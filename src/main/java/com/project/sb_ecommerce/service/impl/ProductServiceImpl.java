package com.project.sb_ecommerce.service.impl;

import com.project.sb_ecommerce.DTOs.Requests.ProductDTO;
import com.project.sb_ecommerce.DTOs.Responses.PaginatedProductResponse;
import com.project.sb_ecommerce.DTOs.Responses.ProductResponse;
import com.project.sb_ecommerce.exceptions.ResourceNotFoundException;
import com.project.sb_ecommerce.model.Category;
import com.project.sb_ecommerce.model.Product;
import com.project.sb_ecommerce.repository.CartRepository;
import com.project.sb_ecommerce.repository.CategoryRepository;
import com.project.sb_ecommerce.repository.ProductRepository;
import com.project.sb_ecommerce.service.Productservice;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements Productservice
{
    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public ProductDTO addProduct( ProductDTO productDTO, long categoryId )
    {
        Category category = categoryRepository.findById( categoryId )
                                     .orElseThrow( () -> new ResourceNotFoundException( "Category", "category id", categoryId ) );

        Product product =  modelMapper.map( productDTO, Product.class );

        double specialPrice = productDTO.getPrice() - ( (productDTO.getDiscount() * 0.01) * productDTO.getPrice() );
        product.setSpecialPrice( specialPrice );

        product.setProductCategory( category );
        Product savedProduct = productRepository.save( product );
        return modelMapper.map( savedProduct, ProductDTO.class );
    }

    @Override
    public PaginatedProductResponse getAllProducts( Integer offset, Integer limit, String sortBy, String sortOrder )
    {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase( "asc" )
                ? Sort.by( sortBy ).ascending() : Sort.by( sortBy ).descending();
        Pageable pagedRequest = PageRequest.of( offset, limit, sortByAndOrder );
        Page<Product> pagedProductList = productRepository.findAll( pagedRequest );

        List<ProductDTO> productDTOList = pagedProductList.getContent().stream()
                .map( product -> modelMapper.map( product, ProductDTO.class ) ).toList();

        PaginatedProductResponse paginatedProductResponse = new PaginatedProductResponse( productDTOList,
                pagedProductList.getSize(), pagedProductList.getNumber(), pagedProductList.getTotalElements(),
                pagedProductList.getTotalPages(), pagedProductList.isLast() );
        return paginatedProductResponse;
    }

    @Override
    public PaginatedProductResponse getProductsByCategory(Long categoryId, Integer offset, Integer limit, String sortBy, String sortOrder )
    {
        Category category = categoryRepository.findById( categoryId )
                .orElseThrow( () -> new ResourceNotFoundException( "Category", "category id", categoryId ) );

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase( "asc" )
                ? Sort.by( sortBy ).ascending() : Sort.by( sortBy ).descending();
        Pageable pagedRequest = PageRequest.of( offset, limit, sortByAndOrder );
        Page<Product> pagedProductList = productRepository.findByProductCategory( category, pagedRequest );

        List<ProductDTO> productDTOList = pagedProductList.getContent().stream()
                .map( product -> modelMapper.map( product, ProductDTO.class ) ).toList();

        PaginatedProductResponse paginatedProductResponse = new PaginatedProductResponse( productDTOList,
                pagedProductList.getSize(), pagedProductList.getNumber(), pagedProductList.getTotalElements(),
                pagedProductList.getTotalPages(), pagedProductList.isLast() );
        return paginatedProductResponse;
    }

    @Override
    public PaginatedProductResponse searchByKeyword( String keyword, Integer offset, Integer limit, String sortBy, String sortOrder )
    {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase( "asc" )
                ? Sort.by( sortBy ).ascending() : Sort.by( sortBy ).descending();
        Pageable pagedRequest = PageRequest.of( offset, limit, sortByAndOrder );

        Page<Product> pagedProductList = productRepository.findByProductNameLikeIgnoreCase( "%" + keyword + "%", pagedRequest );
        List<ProductDTO> productDTOList = pagedProductList.getContent().stream()
                .map( product -> modelMapper.map( product, ProductDTO.class ) ).toList();

        PaginatedProductResponse paginatedProductResponse = new PaginatedProductResponse( productDTOList,
                pagedProductList.getSize(), pagedProductList.getNumber(), pagedProductList.getTotalElements(),
                pagedProductList.getTotalPages(), pagedProductList.isLast() );
        return paginatedProductResponse;
    }

    @Override
    public ProductResponse updateProduct( Long productId, ProductDTO productDTO )
    {
        Product productFromDb = productRepository.findById( productId )
                .orElseThrow( () -> new ResourceNotFoundException( "Product", "product id", productId ) );

        Product product = modelMapper.map( productDTO, Product.class);

        productFromDb.setProductName( product.getProductName() );
        productFromDb.setDescription( product.getDescription() );
        productFromDb.setQuantity( product.getQuantity() );
        productFromDb.setDiscount( product.getDiscount() );
        productFromDb.setPrice( product.getPrice() );
        productFromDb.setSpecialPrice( product.getSpecialPrice() );

        Product savedProduct = productRepository.save( productFromDb );
        return modelMapper.map( savedProduct, ProductResponse.class );
    }


    @Override
    public ProductDTO updateProductImage( Long productId, MultipartFile image ) throws IOException
    {
        Product savedProduct = productRepository.findById( productId )
                .orElseThrow( () -> new ResourceNotFoundException( "Product", "product id", productId ) );

        String path = "images/";
        String fileName = uploadFile( path, image );

        savedProduct.setProductImage( fileName );
        Product imageUpdatedProduct = productRepository.save( savedProduct );
        return modelMapper.map( imageUpdatedProduct, ProductDTO.class );
    }

    private String uploadFile( String path, MultipartFile file ) throws IOException
    {
        String originalName = file.getOriginalFilename();

        // Generate a unique name for each image, so that application can identify without ambiguity.
        String generatedUniqueId = UUID.randomUUID().toString();

        // generatedUniqueId --> generatedUniqueId.jpg
        String uniqueFileName = generatedUniqueId.concat( originalName.substring( originalName.lastIndexOf('.') ) );
        String filePath = path + File.separator + uniqueFileName;

        File folder = new File( path );
        if( !folder.exists() )
            folder.mkdir();

        Files.copy( file.getInputStream(), Paths.get(filePath) );

        return uniqueFileName;
    }

    @Override
    public String deleteProduct( Long productId )
    {
        try{
            productRepository.deleteById( productId );
        } catch (RuntimeException e) {
            throw new RuntimeException( "Product could not be deleted" );
        }
        return "Product deleted successfully !";
    }
}
