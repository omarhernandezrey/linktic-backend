package com.linktic.inventory.model;

import jakarta.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "product_id"))
public class Inventory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "product_id")
    private Long productId;
    private Long quantity;

    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public Long getProductId(){return productId;} public void setProductId(Long productId){this.productId=productId;}
    public Long getQuantity(){return quantity;} public void setQuantity(Long quantity){this.quantity=quantity;}
}
