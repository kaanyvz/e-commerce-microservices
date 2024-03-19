package com.ky.productservice.service;

import com.ky.productservice.dto.CommentDto;
import com.ky.productservice.dto.ProdDto;
import com.ky.productservice.mapper.CommentMapper;
import com.ky.productservice.mapper.ProductMapper;
import com.ky.productservice.model.Category;
import com.ky.productservice.model.ElasticProduct;
import com.ky.productservice.model.Product;
import com.ky.productservice.repository.ElasticRepository;
import com.ky.productservice.repository.ProductRepository;
import com.ky.productservice.request.create.CreateProductRequest;
import com.ky.productservice.request.update.UpdateProductRequest;
import com.ky.rabbitservice.producer.MessageProducer;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CommentMapper commentMapper;
    private final CategoryService categoryService;
    private final ElasticRepository elasticRepository;
    private final MessageProducer messageProducer;
    private final ElasticsearchOperations elasticsearchOperations;
    private final Logger logger = LoggerFactory.getLogger(ProductService.class);

    public ProductService(ProductRepository productRepository,
                          ProductMapper productMapper,
                          CommentMapper commentMapper,
                          CategoryService categoryService,
                          ElasticRepository elasticRepository,
                          MessageProducer messageProducer,
                          ElasticsearchOperations elasticsearchOperations) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.commentMapper = commentMapper;
        this.categoryService = categoryService;
        this.elasticRepository = elasticRepository;
        this.messageProducer = messageProducer;
        this.elasticsearchOperations = elasticsearchOperations;
    }


    @Transactional
    public ProdDto createProduct(CreateProductRequest request){
        Category category = categoryService.getCategoryById(request.getCategoryId());
        Product product = Product.productBuilder()
                .name(request.getName())
                .category(category)
                .unitPrice(request.getPrice())
                .imageUrl(request.getImageUrl())
                .description(request.getDesc())
                .comments(new ArrayList<>())
                .stockCount(request.getStockCount())
                .build();
        product.setCreatedDate(LocalDateTime.now());
        Product savedProduct = productRepository.save(product);
        saveProdInElasticsearch(savedProduct);
        return productMapper.productConverter(savedProduct);
    }


    public ProdDto updateProduct(UpdateProductRequest updateProductRequest, Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(()->{
                    throw  new RuntimeException("Error while finding product!");
                });

        Category category = categoryService.getCategoryById(updateProductRequest.getCategoryId());

        product.setCategory(category);
        product.setDescription(updateProductRequest.getDesc());
        product.setUnitPrice(updateProductRequest.getPrice());
        product.setImageUrl(updateProductRequest.getImageUrl());
        product.setName(updateProductRequest.getNewProductName());

        productRepository.save(product);
        updateProdInElasticsearch(product);
        return productMapper.productConverter(product);
    }

    public Product getProductById(Integer id){
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Could not found."));
    }

    public ProdDto getProductDtoById(Integer id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error"));

        return productMapper.productConverter(product);
    }

    public List<CommentDto> getComments(Integer id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error while finding the product..."));
        return product.getComments().stream().map(commentMapper::commentConverter).collect(Collectors.toList());
    }


    @Transactional
    public String deleteProduct(Integer id) {
        productRepository.deleteById(id);
        elasticRepository.deleteById(id);;
        return "Product has deleted successfully.";
    }

    public List<ProdDto> getAll(){
        return productRepository.findAll().stream().map(productMapper::productConverter).collect(Collectors.toList());
    }

//    public String printIsInStock(Integer productId){
//        if(isInStock(productId)){
//            System.out.println("");
//        }
//
//    }

    public boolean isInStock(Integer productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product cannot find by id."));
        return product.getStockCount() > 0;
    }

    //PRIVATE METHODS
    private void saveProdInElasticsearch(Product product){
        ElasticProduct elasticProduct = ElasticProduct.builder()
                .id(product.getId())
                .categoryName(product.getCategory().getName())
                .unitPrice(product.getUnitPrice())
                .description(product.getDescription())
                .imageUrl(product.getImageUrl())
                .createdDate(LocalDate.now())
                .name(product.getName())
                .build();

        System.out.println(elasticProduct);
        elasticRepository.save(elasticProduct);
    }

    private void updateProdInElasticsearch(Product product){
        ElasticProduct productModel = ElasticProduct.builder()
                .categoryName(product.getCategory().getName())
                .description(product.getDescription())
                .id(product.getId())
                .name(product.getName())
                .unitPrice(product.getUnitPrice())
                .imageUrl(product.getImageUrl())
                .build();
        elasticRepository.save(productModel);
    }

}

















