package com.app.tradogt.dto;

import lombok.Data;

@Data
public class UpdateAddressRequestDto {
    private String orderId;
    private String newAddress;
    private String sessionId;
}
