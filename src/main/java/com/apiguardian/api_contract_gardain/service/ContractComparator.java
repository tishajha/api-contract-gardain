package com.apiguardian.api_contract_gardain.service;

import com.apiguardian.api_contract_gardain.model.ApiEndpoint;
import com.apiguardian.api_contract_gardain.model.ApiField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContractComparator {

    public boolean hasTypeChanged(
            ApiField oldField,
            ApiField newField
    ){
        String oldFieldType= oldField.getFieldType();
        String newFieldType= newField.getFieldType();

        return  !oldFieldType.equalsIgnoreCase(newFieldType);
    }
    public boolean hasRequiredChanged(
            ApiField oldField,
            ApiField newField
    ){
        return oldField.isRequired() != newField.isRequired();
    }
    public List<String> compareEndpoints(
            ApiEndpoint oldEndpoint,
            ApiEndpoint newEndpoint
    ) {
        List<String> changes = new ArrayList<>();
        HashMap<String, ApiField> newEndpointFields = new HashMap<>();

        HashMap<String, ApiField> oldEndpointFields = new HashMap<>();


        for (ApiField field : newEndpoint.responseFields()) {
            newEndpointFields.put(field.getFieldName(), field);
        }

        for (ApiField field : oldEndpoint.responseFields()) {
            oldEndpointFields.put(field.getFieldName(), field);
        }
        for (ApiField oldfield : oldEndpoint.responseFields()) {
            ApiField newField = newEndpointFields.get(oldfield.getFieldName());

            if (newField == null) {
                changes.add("FIELD_REMOVED: " + oldfield.getFieldName());
                continue;
            }

            if (hasTypeChanged(oldfield, newField)) {
                changes.add("TYPE_CHANGED: " + oldfield.getFieldName() + "[ " + oldfield.getFieldType() + " -> " + newField.getFieldType() + " ]");
            }

            if (oldfield.isRequired() && !newField.isRequired()) {
                changes.add("BECAME_OPTIONAL: " + oldfield.getFieldName());
            }
            if(!oldfield.isRequired() && newField.isRequired()){
                changes.add("BECAME_REQUIRED: " + oldfield.getFieldName());
            }
        }

        for (ApiField newField : newEndpoint.responseFields()) {
            if (!oldEndpointFields.containsKey(newField.getFieldName())) {
                changes.add("FIELD_ADDED: " + newField.getFieldName());
            }
        }
        return changes;
    }

    public boolean isCompatible(
            ApiEndpoint oldEndpoint,
            ApiEndpoint newEndpoint
    ){
        List<String> changes= compareEndpoints(oldEndpoint, newEndpoint);

        for(String change: changes){
            if(change.startsWith("FIELD_REMOVED")|| change.startsWith("TYPE_CHANGED")|| change.startsWith("BECAME_REQUIRED")){
                return false;
            }
        }
        return true;
    }

}
