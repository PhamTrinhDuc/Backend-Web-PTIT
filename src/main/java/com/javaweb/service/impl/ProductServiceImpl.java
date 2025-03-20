package com.javaweb.service.impl;

import com.javaweb.dto.ProductDTO;
import com.javaweb.exception.NotFoundException;
import com.javaweb.model.ProductsEntity;
import com.javaweb.model.ResponseObject;
import com.javaweb.repository.ProductRepository;
import com.javaweb.service.ProductService;
import com.javaweb.converter.ProductConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductConverter productConverter;

    public ResponseObject<Page<ProductDTO>> findAllProducts(Pageable pageable) {
        try {
            Page<ProductsEntity> pageProduct = productRepository.findAll(pageable);

            Page<ProductDTO> pageProductDTO = pageProduct.map(productConverter::toDTO);

            return ResponseObject.success(pageProductDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.error("Failed to fetch products", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseObject<ProductDTO> findProductById(Long id){
        try {
            ProductsEntity productsEntity = productRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Product not found with id: " + id));

            ProductDTO productDTO = productConverter.toDTO(productsEntity);

            return ResponseObject.success(productDTO);
        } catch (NotFoundException e) {
            return ResponseObject.error(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.error("Failed to fetch category by id", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseObject<ProductsEntity> saveOrUpdateProduct(ProductDTO productDTO){
        try{
            Long id = productDTO.getId();
            if (id == null && productRepository.existsByName(productDTO.getName())) {
                return ResponseObject.error("Product already exists", HttpStatus.BAD_REQUEST);
            }

            ProductsEntity productsEntity;
            if(id!= null){
                productsEntity = productRepository.findById(id)
                        .orElse(new ProductsEntity());
            }
            else {
                productsEntity = new ProductsEntity();
            }
            productsEntity = productConverter.toEntity(productDTO);
            productRepository.save(productsEntity);
            return ResponseObject.success(productsEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.error("Failed to save or update product", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseObject<Void> deleteProduct(Long id){
        if(id == null){
            return ResponseObject.error("ID must not be null", HttpStatus.BAD_REQUEST);
        }
        if(!productRepository.existsById(id)){
            return ResponseObject.error("Product not found", HttpStatus.NOT_FOUND);
        }
        productRepository.deleteById(id);
        return ResponseObject.success(null);
    }
}
