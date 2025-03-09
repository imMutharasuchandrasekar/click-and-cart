package com.project.sb_ecommerce.service.impl;

import com.project.sb_ecommerce.DTOs.ProductDTO;
import com.project.sb_ecommerce.DTOs.ProductResponse;
import com.project.sb_ecommerce.exceptions.ResourceNotFoundException;
import com.project.sb_ecommerce.model.Category;
import com.project.sb_ecommerce.model.Product;
import com.project.sb_ecommerce.repository.CategoryRepository;
import com.project.sb_ecommerce.repository.ProductRepository;
import com.project.sb_ecommerce.service.Productservice;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements Productservice {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

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
    public ProductResponse getAllProducts()
    {
        List<Product> productList = productRepository.findAll();

        List<ProductDTO> productDTOList = productList.stream()
                .map( product -> modelMapper.map( product, ProductDTO.class ) ).toList();

        ProductResponse responseObj = new ProductResponse();
        responseObj.setContents( productDTOList );
        return responseObj;
    }

    @Override
    public ProductResponse getProudctsByCategory( Long categoryId )
    {
        Category category = categoryRepository.findById( categoryId )
                .orElseThrow( () -> new ResourceNotFoundException( "Category", "category id", categoryId ) );
        List<Product> productList = productRepository.findByProductCategoryOrderByPriceAsc( category );

        List<ProductDTO> productDTOList = productList.stream()
                .map( product -> modelMapper.map( product, ProductDTO.class ) ).toList();

        ProductResponse responseObj = new ProductResponse();
        responseObj.setContents( productDTOList );
        return responseObj;
    }

    @Override
    public ProductResponse searchByKeyword( String keyword )
    {
        List<Product> productList = productRepository.findByProductNameLikeIgnoreCase( "%" + keyword + "%" );
        List<ProductDTO> productDTOList = productList.stream()
                .map( product -> modelMapper.map( product, ProductDTO.class ) ).toList();

        ProductResponse responseObj = new ProductResponse();
        responseObj.setContents( productDTOList );
        return responseObj;
    }

    @Override
    public ProductDTO updateProductImage( Long productId, MultipartFile image ) throws IOException {
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
}
