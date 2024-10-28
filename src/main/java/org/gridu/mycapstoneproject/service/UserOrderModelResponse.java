package org.gridu.mycapstoneproject.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserOrderModelResponse {

    private String orderNumber;
    private String userName;
    private String phoneNumber;
    private String productCode;
    private String productName;
    private String productId;

    public UserOrderModelResponse(String orderNumber, String productCode, String userName, String phoneNumber) {
        this.orderNumber = orderNumber;
        this.productCode = productCode;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
    }
}
