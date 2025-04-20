package com.javaweb.service.impl;

import com.javaweb.dto.ProductDTO;
import com.javaweb.dto.UpdateProductRequestDTO;
import com.javaweb.exception.NotFoundException;
import com.javaweb.model.*;
import com.javaweb.repository.SupplierRespository;
import org.modelmapper.ModelMapper;
import com.javaweb.dto.AddProductRequestDTO;
import com.javaweb.repository.CategoryRepository;
import com.javaweb.repository.ProductRepository;
import com.javaweb.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.Collections;
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
    @Autowired
    private SupplierRespository supplierRepository;

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

    public ResponseObject<ProductsEntity> updateProduct(UpdateProductRequestDTO productDTO) {
        try {
            // Kiểm tra product tồn tại
            Long id = productDTO.getProductId();
            ProductsEntity existingProduct = productRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Product not found with id: " + id));

            // Kiểm tra tên sản phẩm (nếu đổi tên)
            if (productDTO.getProductName() != null && !productDTO.getProductName().trim().isEmpty()) {
                if (!existingProduct.getName().equals(productDTO.getProductName()) &&
                        productRepository.existsByName(productDTO.getProductName())) {
                    return ResponseObject.error("Product with name '" + productDTO.getProductName() + "' already exists", HttpStatus.BAD_REQUEST);
                }
                existingProduct.setName(productDTO.getProductName());
            }

            // Cập nhật description
            existingProduct.setDescription(productDTO.getDescription());

            // Cập nhật giá
            if (productDTO.getPrice() != null) {
                existingProduct.setPrice(productDTO.getPrice());
            }

            // Cập nhật discount
            if (productDTO.getDiscount() != null) {
                existingProduct.setDiscount(Math.max(0.0, productDTO.getDiscount()));
            }

            // Cập nhật quantity
            if (productDTO.getQuantityStock() != null) {
                existingProduct.setQuantityStock(Math.max(0, productDTO.getQuantityStock()));
            }

            // Cập nhật category (nếu thay đổi)
            if (productDTO.getCategory() != null && !productDTO.getCategory().trim().isEmpty()) {
                CategoryEntity category = categoryRepository.findByName(productDTO.getCategory())
                        .orElseThrow(() -> new NotFoundException("Category not found with name: " + productDTO.getCategory()));
                existingProduct.setCategory(category);
            }

            // Cập nhật supplier (nếu thay đổi)
            if (productDTO.getSupplier() != null && !productDTO.getSupplier().trim().isEmpty()) {
                SupplierEntity supplier = supplierRepository.findByName(productDTO.getSupplier())
                        .orElseThrow(() -> new NotFoundException("Supplier not found with name: " + productDTO.getSupplier()));
                existingProduct.setSupplier(supplier);
            }

            // Cập nhật specification
            if (productDTO.getSpecification() != null) {
                existingProduct.setSpecification(productDTO.getSpecification());
            }

            // Chỉ xử lý nếu có ảnh mới
            if (productDTO.getImagePaths() != null && !productDTO.getImagePaths().isEmpty()) {
                for (String url : productDTO.getImagePaths()) {
                    if (url != null && !url.trim().isEmpty()) {
                        ProductImageEntity img = new ProductImageEntity();
                        img.setImagePath(url);
                        img.setProducts(existingProduct);  // thiết lập quan hệ N-1
                        existingProduct.getProductImageEntities().add(img);
                    }
                }
            }


            // Cập nhật thời gian
            existingProduct.setUpdated(new Timestamp(System.currentTimeMillis()));

            // Lưu lại
            ProductsEntity saved = productRepository.save(existingProduct);
            return ResponseObject.success(saved);
        } catch (NotFoundException e) {
            return ResponseObject.error(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.error("Failed to update product: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseObject<List<ProductDTO>> findProductByPriceRange(Double minPrice, Double maxPrice) {
        try {
            List<ProductsEntity> products;

            if (minPrice != null && maxPrice != null) {
                if (minPrice > maxPrice) {
                    return ResponseObject.error("Invalid price range", HttpStatus.BAD_REQUEST);
                }
                products = productRepository.findByPriceBetween(minPrice, maxPrice);

            } else if (minPrice != null) {
                products = productRepository.findByPriceGreaterThanEqual(minPrice);

            } else if (maxPrice != null) {
                products = productRepository.findByPriceLessThanEqual(maxPrice);

            } else {
                return ResponseObject.error("Price range must be specified", HttpStatus.BAD_REQUEST);
            }

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
                case "newest":
                    products = productRepository.findAllByOrderByCreatedAtDesc();
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

    public ResponseObject<ProductsEntity> createNewProduct(AddProductRequestDTO productDTO) {
        try {
            // Kiểm tra tên sản phẩm
            if (productDTO.getProductName() == null || productDTO.getProductName().trim().isEmpty()) {
                return ResponseObject.error("Product name is required", HttpStatus.BAD_REQUEST);
            }
            if (productRepository.existsByName(productDTO.getProductName())) {
                return ResponseObject.error("Product with name '" + productDTO.getProductName() + "' already exists", HttpStatus.BAD_REQUEST);
            }

            // Kiểm tra category
            if (productDTO.getCategory() == null || productDTO.getCategory().trim().isEmpty()) {
                return ResponseObject.error("Category is required", HttpStatus.BAD_REQUEST);
            }
            CategoryEntity category = categoryRepository.findBySlug(productDTO.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category not found with slug: " + productDTO.getCategory()));

            // Kiểm tra supplier
            String supplierName = productDTO.getSupplier();
            if (supplierName == null || supplierName.trim().isEmpty()) {
                return ResponseObject.error("Supplier is required", HttpStatus.BAD_REQUEST);
            }
            SupplierEntity supplier = supplierRepository.findByName(supplierName)
                    .orElseThrow(() -> new NotFoundException("Supplier not found with name: " + supplierName));

            // Tạo ProductsEntity
            ProductsEntity newProduct = new ProductsEntity();
            newProduct.setName(productDTO.getProductName());
            newProduct.setDescription(productDTO.getDescription());
            newProduct.setPrice(productDTO.getPrice() != null ? productDTO.getPrice() : 0.0);
            newProduct.setDiscount(productDTO.getDiscount() != null ? productDTO.getDiscount() : 0.0);
            newProduct.setQuantityStock(productDTO.getQuantityStock() != null ? productDTO.getQuantityStock() : 0);
            newProduct.setCategory(category);
            newProduct.setSupplier(supplier);

            // Xử lý danh sách ảnh
            List<String> imageUrls = productDTO.getImagePaths();
            if (imageUrls != null && !imageUrls.isEmpty()) {
                List<ProductImageEntity> imageEntities = imageUrls.stream()
                        .filter(url -> url != null && !url.trim().isEmpty())
                        .map(url -> {
                            ProductImageEntity image = new ProductImageEntity();
                            image.setImagePath(url);
                            image.setProducts(newProduct);
                            return image;
                        })
                        .collect(Collectors.toList());
                newProduct.setProductImageEntities(imageEntities);
            }
            // Xử lý specification
            if (productDTO.getSpecification() != null && !productDTO.getSpecification().isEmpty()) {
                newProduct.setSpecification(productDTO.getSpecification());
            }

            // Thiết lập giá trị mặc định
            newProduct.setCreated(new Timestamp(System.currentTimeMillis()));
            newProduct.setUpdated(new Timestamp(System.currentTimeMillis()));
            if (newProduct.getQuantityStock() < 0) {
                newProduct.setQuantityStock(0);
            }
            if (newProduct.getDiscount() < 0) {
                newProduct.setDiscount(0.0);
            }

            // Lưu sản phẩm
            ProductsEntity savedProduct = productRepository.save(newProduct);
            return ResponseObject.success(savedProduct);
        } catch (NotFoundException e) {
            return ResponseObject.error(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.error("Failed to create new product: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseObject<List<ProductDTO>> findProductsByName(String keyword) {
        try {
            // Tìm kiếm sản phẩm trong database, không phân biệt hoa thường
            List<ProductsEntity> products = productRepository.findByNameContainingIgnoreCase(keyword);

            // Chuyển đổi từ ProductsEntity sang ProductDTO
            List<ProductDTO> productDTO = products.stream()
                    .map(product -> new ProductDTO(product)) // Giả sử ProductDTO có constructor này
                    .collect(Collectors.toList());

            return ResponseObject.success(productDTO);
        } catch (NotFoundException e) {
        return ResponseObject.error(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ResponseObject.error(
                    "Error while searching products: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
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
