package com.ky.productservice.repository;

import com.ky.productservice.model.ElasticProduct;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ElasticRepository extends ElasticsearchRepository<ElasticProduct, Integer> {
}
