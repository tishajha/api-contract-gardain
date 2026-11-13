package com.apiguardian.api_contract_gardain.dto;

import java.time.LocalDateTime;
import java.util.Map;

public record ValidationErrorResponse(
        LocalDateTime timeStamp,
        int status,
        String error,
        String message,
        Map<String, String> fieldError
) {
}
