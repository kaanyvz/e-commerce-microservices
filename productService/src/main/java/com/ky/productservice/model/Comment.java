package com.ky.productservice.model;

import com.ky.commons.model.AdvanceBaseModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comments")
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "commentBuilder")
@Data
public class Comment extends AdvanceBaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String text;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private String commentCreator;

}
