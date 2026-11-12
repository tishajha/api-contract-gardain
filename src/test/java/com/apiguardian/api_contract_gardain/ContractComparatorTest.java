package com.apiguardian.api_contract_gardain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ContractComparatorTest {

    private ContractComparator comparator;

    @BeforeEach
    void setUp() {
        comparator = new ContractComparator();
    }



    @Test
    void hasTypeChanged_differentType_returnsTrue() {
        ApiField oldField = new ApiField("id", "INTEGER", true);
        ApiField newField = new ApiField("id", "STRING", true);
        assertTrue(comparator.hasTypeChanged(oldField, newField));
    }
    @Test
    void hasTypeChanged_sameType_returnsFalse() {
        ApiField oldField = new ApiField("id", "INTEGER", true);
        ApiField newField = new ApiField("id", "INTEGER", true);
        assertFalse(comparator.hasTypeChanged(oldField, newField));
    }

    @Test
    void hasTypeChanged_caseDifferentType_returnsFalse() {
        ApiField oldField = new ApiField("id", "INTEGER", true);
        ApiField newField = new ApiField("id", "integer", true);
        assertFalse(comparator.hasTypeChanged(oldField, newField));
    }
    @Test
    void hasTypeChanged_nullOldType_throwsNullPointerException() {
        ApiField oldField = new ApiField("id", null , true);
        ApiField newField = new ApiField("id", "INTEGER", true);
        assertThrows(NullPointerException.class,()->comparator.hasTypeChanged(oldField, newField));
    }

    @Test
    void hasRequiredChanged_optional_to_required() {
        ApiField oldfield = new ApiField("id", "STRING", false);
        ApiField newfield = new ApiField("id", "STRING", true);
        assertTrue(comparator.hasRequiredChanged(oldfield,newfield));

    }

    @Test
    void hasRequiredChanged_required_to_optional() {
        ApiField oldfield = new ApiField("id", "STRING", true);
        ApiField newfield = new ApiField("id", "STRING", false);
        assertTrue(comparator.hasRequiredChanged(oldfield,newfield));

    }
    @Test
    void hasRequiredChanged_optional_to_optional() {
        ApiField oldfield = new ApiField("id", "STRING", false);
        ApiField newfield = new ApiField("id", "STRING", false);
        assertFalse(comparator.hasRequiredChanged(oldfield,newfield));

    }
    @Test
    void hasRequiredChanged_required_to_required() {
        ApiField oldfield = new ApiField("id", "STRING", true);
        ApiField newfield = new ApiField("id", "STRING", true);
        assertFalse(comparator.hasRequiredChanged(oldfield,newfield));

    }
    @Test
    void compareEndpoints_whenFieldRemoved_returnsFieldRemoved() {
        ApiEndpoint oldEndpoint = new ApiEndpoint("/users", "GET",
                List.of(new ApiField("id", "INTEGER", true), new ApiField("name", "STRING", true)));
        ApiEndpoint newEndpoint = new ApiEndpoint("/users", "GET",
                List.of(new ApiField("id", "INTEGER", true)));
        List<String> changes = comparator.compareEndpoints(oldEndpoint, newEndpoint);
        assertTrue(changes.stream().anyMatch(c -> c.startsWith("FIELD_REMOVED: name")));
    }
    @Test
    void compareEndpoints_whenFieldAdded_returnFieldAdded(){
        ApiEndpoint oldEndpoint = new ApiEndpoint("/users", "GET",
                List.of(new ApiField("id", "INTEGER", true)));
        ApiEndpoint newEndpoint = new ApiEndpoint("/users", "GET",
                List.of(new ApiField("id", "INTEGER", true), new ApiField("name", "STRING", true)));

        List<String> changes = comparator.compareEndpoints(oldEndpoint, newEndpoint);
        assertTrue(changes.stream().anyMatch(c->c.startsWith("FIELD_ADDED: name")));

    }
    @Test
    void compareEndpoints_whenTypeChanged_returnFieldNameChanged_from_to(){

        ApiEndpoint oldEndpoint = new ApiEndpoint("/users", "GET",
                List.of(new ApiField("id", "STRING", true)));
        ApiEndpoint newEndpoint = new ApiEndpoint("/users", "GET",
                List.of(new ApiField("id", "INTEGER", true)));
        List<String> changes = comparator.compareEndpoints(oldEndpoint, newEndpoint);
        assertTrue(changes.stream().anyMatch(c->c.startsWith("TYPE_CHANGED: id[ STRING -> INTEGER ]")));

    }
    @Test
    void compareEndpoints_whenFieldBecameOptional_returnsBecameOptional() {
        // Arrange
        ApiField oldEmail = new ApiField("email", "STRING", true);
        ApiField oldId = new ApiField("id", "STRING", true);

        ApiEndpoint oldEndpoint = new ApiEndpoint("/users", "GET",
                List.of(oldId, oldEmail));

        ApiField newEmail = new ApiField("email", "STRING", false);
        ApiField newId = new ApiField("id", "INTEGER", true);

        ApiEndpoint newEndpoint = new ApiEndpoint("/users", "GET",
                List.of(newId, newEmail));


        List<String> changes = comparator.compareEndpoints(oldEndpoint, newEndpoint);


        boolean foundBecameOptional = changes.stream()
                .anyMatch(c -> c.startsWith("BECAME_OPTIONAL: " + oldEmail.getFieldName()));

        assertTrue(foundBecameOptional);
    }
//    No changes	[id, name]	[id, name]	Empty list	Identical endpoints
//    Same name, different order	[id, name]	[name, id]	Empty list (if types match)	Order-independent comparison
    @Test
    void compareEndpoint_when_there_is_noChange(){
        ApiField oldEmail= new ApiField("email", "STRING", true);
        ApiField oldId = new ApiField("id","INTEGER", true);

        ApiEndpoint oldEndpoint= new ApiEndpoint("/user","GET", List.of(oldId,oldEmail));

        ApiField newEmail= new ApiField("email", "STRING", true);
        ApiField newId = new ApiField("id","INTEGER", true);

        ApiEndpoint newEndpoint = new ApiEndpoint("/user", "GET", List.of(newEmail,newId));

        boolean foundChange = comparator.isCompatible(oldEndpoint, newEndpoint);

        assertTrue(foundChange);
    }

    @Test
    void isCompatible_fieldAdded() {
//        ApiField oldEmail= new ApiField("email", "STRING", true);
        ApiField oldId = new ApiField("id","INTEGER", true);

        ApiEndpoint oldEndpoint= new ApiEndpoint("/user","GET", List.of(oldId));

        ApiField newEmail= new ApiField("email", "STRING", true);
        ApiField newId = new ApiField("id","INTEGER", true);

        ApiEndpoint newEndpoint = new ApiEndpoint("/user", "GET", List.of(newEmail,newId));

        boolean foundFieldRemoved = comparator.isCompatible(oldEndpoint, newEndpoint);

        assertTrue(foundFieldRemoved);


    }
    @Test
    void isCompatible_fieldRemoved_returnFalse() {
        ApiField oldEmail= new ApiField("email", "STRING", true);
        ApiField oldId = new ApiField("id","INTEGER", true);

        ApiEndpoint oldEndpoint= new ApiEndpoint("/user","GET", List.of(oldId , oldEmail));

//        ApiField newEmail= new ApiField("email", "STRING", true);
        ApiField newId = new ApiField("id","INTEGER", true);

        ApiEndpoint newEndpoint = new ApiEndpoint("/user", "GET", List.of(newId));


        boolean result = comparator.isCompatible(oldEndpoint,newEndpoint);

        assertFalse(result);


    }

    @Test
    void isCompatible_fieldBecameOptional_returnTrue() {
        ApiField oldEmail= new ApiField("email", "STRING", true);
        ApiField oldId = new ApiField("id","INTEGER", true);

        ApiEndpoint oldEndpoint= new ApiEndpoint("/user","GET", List.of(oldId , oldEmail));

        ApiField newEmail= new ApiField("email", "STRING", false);
        ApiField newId = new ApiField("id","INTEGER", true);

        ApiEndpoint newEndpoint = new ApiEndpoint("/user", "GET", List.of(newId, newEmail));


        boolean result = comparator.isCompatible(oldEndpoint,newEndpoint);

        assertTrue(result);


    }
    @Test
    void isCompatible_fieldBecameRequired_returnsFalse() {
        ApiField oldEmail= new ApiField("email", "STRING", false);
        ApiField oldId = new ApiField("id","INTEGER", true);

        ApiEndpoint oldEndpoint= new ApiEndpoint("/user","GET", List.of(oldId , oldEmail));

        ApiField newEmail= new ApiField("email", "STRING", true);
        ApiField newId = new ApiField("id","INTEGER", true);

        ApiEndpoint newEndpoint = new ApiEndpoint("/user", "GET", List.of(newId, newEmail));

      boolean result = comparator.isCompatible(oldEndpoint, newEndpoint);

        assertFalse(result);
    }

    @Test
    void isCompatible_fieldTypeChange_returnsFalse() {
        ApiField oldEmail= new ApiField("email", "STRING", false);
        ApiField oldId = new ApiField("id","INTEGER", true);

        ApiEndpoint oldEndpoint= new ApiEndpoint("/user","GET", List.of(oldId , oldEmail));

        ApiField newEmail= new ApiField("email", "STRING", true);
        ApiField newId = new ApiField("id","STRING", true);

        ApiEndpoint newEndpoint = new ApiEndpoint("/user", "GET", List.of(newId, newEmail));

        boolean result = comparator.isCompatible(oldEndpoint, newEndpoint);

        assertFalse(result);
    }
}