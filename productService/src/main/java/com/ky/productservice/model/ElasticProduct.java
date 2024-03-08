package com.ky.productservice.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "product")
@Setting(settingPath = "/es-settings.json")
public class ElasticProduct implements Serializable {
    @Id
    private Integer id;
    private String imageUrl;
    private String name;
    private BigDecimal unitPrice;
    private String categoryName;
    @Field(type= FieldType.Date, format={}, pattern="yyyy-MM-dd")
    private LocalDate createdDate;
    private String description;
}
