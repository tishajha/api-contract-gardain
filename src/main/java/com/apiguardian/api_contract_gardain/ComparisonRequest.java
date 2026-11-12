package com.apiguardian.api_contract_gardain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class ComparisonRequest {
    @NotBlank
    private String oldPath;

    @NotBlank
    private String oldMethod;

    @NotNull(message = "Old endpoint fields are required")
    @Size(min = 1, message = "At least one old field is required")
    private List<@Valid ApiFieldRequest> oldFields;

    @NotBlank
    private String newPath;

    @NotBlank
    private String newMethod;

    @NotNull
    @Size(min = 1, message = "At least one new field is required")
    private  List<@Valid ApiFieldRequest> newFields;

    public ComparisonRequest() {
    }

    public ComparisonRequest(String oldMethod, String oldPath, List<ApiFieldRequest> oldFields, String newPath, String newMethod, List<ApiFieldRequest> newFields) {
        this.oldMethod = oldMethod;
        this.oldPath = oldPath;
        this.oldFields = oldFields;
        this.newPath = newPath;
        this.newMethod = newMethod;
        this.newFields = newFields;
    }

    public String getOldPath() {
        return oldPath;
    }

    public void setOldPath(String oldPath) {
        this.oldPath = oldPath;
    }

    public String getOldMethod() {
        return oldMethod;
    }

    public void setOldMethod(String oldMethod) {
        this.oldMethod = oldMethod;
    }

    public List<ApiFieldRequest> getOldFields() {
        return oldFields;
    }

    public void setOldFields(List<ApiFieldRequest> oldFields) {
        this.oldFields = oldFields;
    }

    public String getNewMethod() {
        return newMethod;
    }

    public void setNewMethod(String newMethod) {
        this.newMethod = newMethod;
    }

    public String getNewPath() {
        return newPath;
    }

    public void setNewPath(String newPath) {
        this.newPath = newPath;
    }

    public List<ApiFieldRequest> getNewFields() {
        return newFields;
    }

    public void setNewFields(List<ApiFieldRequest> newFields) {
        this.newFields = newFields;
    }
}
