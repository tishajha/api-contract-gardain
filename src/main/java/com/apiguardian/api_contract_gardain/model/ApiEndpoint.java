package com.apiguardian.api_contract_gardain.model;

import java.util.List;

public record ApiEndpoint(
        String path,
        String method,
        List<ApiField> responseFields
) {
}
