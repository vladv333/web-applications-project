package com.simpleshop.dto;

import lombok.Data;
import java.util.List;

@Data
public class OrderRequest {

    private String customerName;
    private List<ItemRequest> items;

    @Data
    public static class ItemRequest {
        private Long productId;
        private Integer quantity;
    }
}
