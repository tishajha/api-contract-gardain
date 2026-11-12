package com.apiguardian.api_contract_gardain;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/contracts")
public class ContractComparisonController {
    private final ContractComparator comparator = new ContractComparator();


    @PostMapping("/compare")
    public ResponseEntity<ComparisonResult> compare (@Valid @RequestBody ComparisonRequest request){
        ApiEndpoint oldEndpoint = toDomain(
                request.getOldPath(),
                request.getOldMethod(),
                request.getOldFields()
        );

        ApiEndpoint newEndpoint = toDomain(
                request.getNewPath(),
                request.getNewMethod(),
                request.getNewFields()
        );

        List<String> changes= comparator.compareEndpoints(oldEndpoint, newEndpoint);
        boolean compatible= comparator.isCompatible(oldEndpoint, newEndpoint);

        ComparisonResult result= new ComparisonResult(compatible, changes);
        return ResponseEntity.ok(result);

    }

    @GetMapping("/health")
    public  ResponseEntity<String > health(){
        return ResponseEntity.ok("API Contract Gardain is running");
    }

    private ApiEndpoint toDomain(String path, String method, List<ApiFieldRequest> fieldRequests) {
        List<ApiField> fields = fieldRequests.stream()
                .map(fr->new ApiField(fr.getFieldName(), fr.getFieldType(), fr.isRequired()))
                .collect(Collectors.toList());

        return new ApiEndpoint(path, method, fields);
    }

}
