package com.chenfeng.symptom.service.syndrome_element;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

public class SyndromeElementInput {
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String syndromeElementStart;

    @NotBlank
    @Size(max = 100)
    private String syndromeElementEnd;

    public String getSyndromeElementStart() {
        return syndromeElementStart;
    }

    public void setSyndromeElementStart(String syndromeElementStart) {
        this.syndromeElementStart = syndromeElementStart;
    }

    public String getSyndromeElementEnd() {
        return syndromeElementEnd;
    }

    public void setSyndromeElementEnd(String syndromeElementEnd) {
        this.syndromeElementEnd = syndromeElementEnd;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}