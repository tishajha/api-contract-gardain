package com.apiguardian.api_contract_gardain.dto;

import java.util.List;

public class ComparisonResult {

    private boolean compatible;

    private List<String> changes;

    public ComparisonResult() {
    }

    public ComparisonResult(boolean compatible, List<String> changes) {
        this.compatible = compatible;
        this.changes = changes;
    }

    public boolean isCompatible() {
        return compatible;
    }

    public void setCompatible(boolean compatible) {
        this.compatible = compatible;
    }

    public List<String> getChanges() {
        return changes;
    }

    public void setChanges(List<String> changes) {
        this.changes = changes;
    }
}
