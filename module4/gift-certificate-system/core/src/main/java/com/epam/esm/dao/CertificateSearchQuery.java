package com.epam.esm.dao;

public class CertificateSearchQuery {

    private String tagName;
    private String partOfName;
    private String partOfDescription;
    private String sortParameter;
    private String sortOrder;

    public boolean hasTagName() {
        return tagName != null;
    }

    public boolean hasPartOfName() {
        return partOfName != null;
    }

    public boolean hasPartOfDescription() {
        return partOfDescription != null;
    }

    public boolean hasSortParameter() {
        return sortParameter != null;
    }

    public boolean hasSortOrder() {
        return sortOrder != null;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getPartOfName() {
        return partOfName;
    }

    public void setPartOfName(String partOfName) {
        this.partOfName = partOfName;
    }

    public String getPartOfDescription() {
        return partOfDescription;
    }

    public void setPartOfDescription(String partOfDescription) {
        this.partOfDescription = partOfDescription;
    }

    public String getSortParameter() {
        return sortParameter;
    }

    public void setSortParameter(String sortParameter) {
        this.sortParameter = sortParameter;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}
