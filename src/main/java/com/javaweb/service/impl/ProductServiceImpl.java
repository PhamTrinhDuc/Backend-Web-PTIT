package com.javaweb.service.impl;

import com.javaweb.dto.ProductDTO;
import com.javaweb.exception.NotFoundException;
import com.javaweb.model.CategoryEntity;
import com.javaweb.model.ProductImageEntity;
import org.modelmapper.ModelMapper;
import com.javaweb.model.ProductsEntity;
import com.javaweb.model.ResponseObject;
import com.javaweb.repository.CategoryRepository;
import com.javaweb.repository.ProductRepository;
import com.javaweb.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper; // Inject ModelMapper

    @Autowired
    private CategoryRepository categoryRepository;

    public ResponseObject<Page<ProductDTO>> findAllProducts(Pageable pageable) {
        try {
            Page<ProductsEntity> pageProduct = productRepository.findAll(pageable);

            Page<ProductDTO> pageProductDTO = pageProduct.map(product -> modelMapper.map(product, ProductDTO.class));

            return ResponseObject.success(pageProductDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.error("Failed to fetch products", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseObject<List<ProductDTO>> findProductsByCategorySlug(String categorySlug) {
        try {
            List<ProductsEntity> products = productRepository.findByCategorySlug(categorySlug);

            if (products.isEmpty()) {
                return ResponseObject.error("No products found for category: " + categorySlug, HttpStatus.NOT_FOUND);
            }

            List<ProductDTO> productDTO = products.stream()
                    .map(product -> modelMapper.map(product, ProductDTO.class))
                    .collect(Collectors.toList());

            return ResponseObject.success(productDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.error("Failed to fetch products by category slug", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseObject<ProductDTO> findProductById(Long id) {
        try {
            ProductsEntity productsEntity = productRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Product not found with id: " + id));

            System.out.println("Product entity: " + productsEntity);

            // Sử dụng ProductMapper
            ProductDTO productDTO = modelMapper.map(productsEntity, ProductDTO.class);

            return ResponseObject.success(productDTO);
        } catch (NotFoundException e) {
            return ResponseObject.error(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.error("Failed to fetch product by id", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseObject<List<ProductDTO>> findProductByDiscount() {
        try {
            List<ProductsEntity> products = productRepository.findByDiscountGreaterThan(0.0); // Giả sử lấy các sản phẩm có discount > 0
            if (products.isEmpty()) {
                return ResponseObject.error("No products with discount found", HttpStatus.NOT_FOUND);
            }
            List<ProductDTO> productDTO = products.stream()
                    .map(product -> modelMapper.map(product, ProductDTO.class))
                    .collect(Collectors.toList());
            return ResponseObject.success(productDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.error("Failed to fetch products by discount", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseObject<ProductDTO> findProductByCategory(Long id) {
        try {
            ProductsEntity productsEntity = productRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Product not found with id: " + id));

            System.out.println("Product entity: " + productsEntity);

            // Sử dụng ProductMapper
            ProductDTO productDTO = modelMapper.map(productsEntity, ProductDTO.class);

            return ResponseObject.success(productDTO);
        } catch (NotFoundException e) {
            return ResponseObject.error(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.error("Failed to fetch product by id", HttpStatus.INTERNAL_SERVER_ERROR);
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
            } else {
                productsEntity = new ProductsEntity();
            }
            productsEntity = modelMapper.map(productDTO, ProductsEntity.class);
            productRepository.save(productsEntity);
            return ResponseObject.success(productsEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.error("Failed to save or update product", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseObject<List<ProductDTO>> findProductByPriceRange(Double minPrice, Double maxPrice) {
        try {
            if (minPrice == null || maxPrice == null || minPrice > maxPrice) {
                return ResponseObject.error("Invalid price range", HttpStatus.BAD_REQUEST);
            }
            List<ProductsEntity> products = productRepository.findByPriceBetween(minPrice, maxPrice);
            if (products.isEmpty()) {
                return ResponseObject.error("No products found in price range", HttpStatus.NOT_FOUND);
            }
            List<ProductDTO> productDTO = products.stream()
                    .map(product -> modelMapper.map(product, ProductDTO.class))
                    .collect(Collectors.toList());
            return ResponseObject.success(productDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.error("Failed to fetch products by price range", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseObject<List<ProductDTO>> findProductsSortedBy(String sortBy) {
        try {
            List<ProductsEntity> products;
            switch (sortBy.toLowerCase()) {
                case "price_asc":
                    products = productRepository.findAllByOrderByPriceAsc();
                    break;
                case "price_desc":
                    products = productRepository.findAllByOrderByPriceDesc();
                    break;
                case "name_asc":
                    products = productRepository.findAllByOrderByNameAsc();
                    break;
                case "name_desc":
                    products = productRepository.findAllByOrderByNameDesc();
                    break;
                default:
                    return ResponseObject.error("Invalid sort criteria", HttpStatus.BAD_REQUEST);
            }
            if (products.isEmpty()) {
                return ResponseObject.error("No products found", HttpStatus.NOT_FOUND);
            }
            List<ProductDTO> productDTO = products.stream()
                    .map(product -> modelMapper.map(product, ProductDTO.class))
                    .collect(Collectors.toList());
            return ResponseObject.success(productDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.error("Failed to fetch sorted products", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseObject<ProductsEntity> createNewProduct(ProductDTO productDTO) {
        try {
            // Kiểm tra tên sản phẩm đã tồn tại
            if (productRepository.existsByName(productDTO.getName())) {
                return ResponseObject.error("Product with name '" + productDTO.getName() + "' already exists", HttpStatus.BAD_REQUEST);
            }

            // Kiểm tra categoryId hợp lệ
            Long categoryId = productDTO.getCategoryId();
            if (categoryId == null || !categoryRepository.existsById(categoryId)) {
                return ResponseObject.error("Invalid or missing category ID", HttpStatus.BAD_REQUEST);
            }

            // Ánh xạ từ ProductDTO sang ProductsEntity
            ProductsEntity newProduct = modelMapper.map(productDTO, ProductsEntity.class);

            // Thiết lập CategoryEntity từ categoryId
            CategoryEntity category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new NotFoundException("Category not found with id: " + categoryId));
            newProduct.setCategory(category);

            // Liên kết ProductImageEntity với ProductsEntity
            List<ProductImageEntity> imageEntities = newProduct.getProductImageEntities();
            if (imageEntities != null) {
                imageEntities.forEach(image -> image.setProducts(newProduct));
            }

            // Thiết lập giá trị mặc định
            if (newProduct.getCreated() == null) {
                newProduct.setCreated(new Timestamp(System.currentTimeMillis()));
            }
            if (newProduct.getUpdated() == null) {
                newProduct.setUpdated(new Timestamp(System.currentTimeMillis()));
            }
            if (newProduct.getQuantityStock() <= 0) {
                newProduct.setQuantityStock(0);
            }
            if (newProduct.getDiscount() < 0) {
                newProduct.setDiscount(0.0);
            }

            // Lưu sản phẩm vào database
            ProductsEntity savedProduct = productRepository.save(newProduct);
            return ResponseObject.success(savedProduct);
        } catch (NotFoundException e) {
            return ResponseObject.error(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.error("Failed to create new product: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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
