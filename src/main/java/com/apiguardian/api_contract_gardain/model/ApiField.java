package com.apiguardian.api_contract_gardain.model;

public class ApiField {
    private final String fieldName;
    private final String fieldType;
    private final boolean required;

    public ApiField(String fieldName, String fieldType, boolean required) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.required = required;
    }

    public boolean isRequired() {
        return required;
    }

    public String getFieldType() {
        return fieldType;
    }

    public String getFieldName() {
        return fieldName;
    }
}
