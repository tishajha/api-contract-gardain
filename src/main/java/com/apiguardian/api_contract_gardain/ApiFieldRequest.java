package com.apiguardian.api_contract_gardain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ApiFieldRequest {
    @NotBlank
    private  String fieldName;
    @NotBlank
    private String fieldType;
    @NotNull
    private boolean required;


    public ApiFieldRequest(){

    }

    public ApiFieldRequest(String fieldName, String fieldType, boolean required){
        this.fieldName= fieldName;
        this.fieldType= fieldType;
        this.required= required;

    }


    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
