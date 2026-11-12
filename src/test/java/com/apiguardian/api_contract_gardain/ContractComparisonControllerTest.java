package com.apiguardian.api_contract_gardain;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ContractComparisonControllerTest {

    private final ContractComparisonController controller = new ContractComparisonController();
    @Test
    void compare_returnsChanges() {
        ComparisonRequest request = new ComparisonRequest(
                "/users", "GET",
                List.of(new ApiFieldRequest("id", "INTEGER", true)),
                "/users", "GET",
                List.of(new ApiFieldRequest("id", "STRING", true))
        );

        ResponseEntity<ComparisonResult> response = controller.compare(request);
        ComparisonResult result = response.getBody();

        assertNotNull(result);
        assertFalse(result.isCompatible());
        assertTrue(result.getChanges().stream().anyMatch(c -> c.startsWith("TYPE_CHANGED: id")));
    }

    @Test
    void health_returnsOk() {
        ResponseEntity<String> response = controller.health();
        assertEquals("API Contract Gardain is running", response.getBody());
    }

}