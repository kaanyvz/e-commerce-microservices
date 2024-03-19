package com.ky.rabbitservice.request;

public class InventoryRequest {
    private Integer productId;
    private  Integer qty;

    public InventoryRequest(Integer productId, Integer qty) {
        this.productId = productId;
        this.qty = qty;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
}
