package com.ecommerce.microcommerce.responses;

import com.ecommerce.microcommerce.model.Product;

public class ProductWithMargin {
    private Product product;
    private int margin;

    public ProductWithMargin(Product product) {
        this.product = product;
        this.margin = product.getPrix() - product.getPrixAchat();
    }

    public Product getProduct() {
        return product;
    }

    public int getMargin() {
        return margin;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }

    @Override
    public String toString() {
        return "ProductWithMargin{" +
                "product=" + product +
                ", margin=" + margin +
                '}';
    }
}
