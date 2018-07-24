package com.crowdar.database;

public class Query {

    private String reference;
    private String keyToOrderBy;
    private String valueEqualTo;
    private String keyChildFinal;
    private String valueToSet;

    public Query() {
        super();
    }

    public Query(String reference, String keyToOrderBy, String valueEqualTo, String keyChildFinal, String valueToSet){
        this();
        this.reference = reference;
        this.keyToOrderBy = keyToOrderBy;
        this.valueEqualTo = valueEqualTo;
        this.keyChildFinal = keyChildFinal;
        this.valueToSet = valueToSet;
    }

    public String getReference() {
        return reference;
    }

    public String getValueEqualTo() {
        return valueEqualTo;
    }

    public String getValueToSet() {
        return valueToSet;
    }

    public String getKeyChildFinal() {
        return keyChildFinal;
    }

    public String getKeyToOrderBy() {
        return keyToOrderBy;
    }
}
