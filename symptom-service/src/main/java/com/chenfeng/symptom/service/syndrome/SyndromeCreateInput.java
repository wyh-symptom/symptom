package com.chenfeng.symptom.service.syndrome;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

public class SyndromeCreateInput {
    private Long id;
    
	@NotBlank
    @Size(max = 100)
    private String symptomName;
	
	@Size(max = 100)
    private String symptomCategory;
    
    public String getSymptomCategory() {
		return symptomCategory;
	}

	public void setSymptomCategory(String symptomCategory) {
		this.symptomCategory = symptomCategory;
	}

	@NotBlank
    @Size(max = 5000)
    private String description;

    @NotBlank
    @Size(max = 100)
    private String syndromeElementStart;

    @NotBlank
    @Size(max = 100)
    private String syndromeElementEnd;

    public String getSymptomName() {
        return symptomName;
    }

    public void setSymptomName(String symptomName) {
        this.symptomName = symptomName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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