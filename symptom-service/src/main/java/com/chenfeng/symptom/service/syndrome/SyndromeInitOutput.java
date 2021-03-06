package com.chenfeng.symptom.service.syndrome;

import java.util.List;

import com.chenfeng.symptom.domain.model.mybatis.Syndrome;


public class SyndromeInitOutput {
    
	private String symptomName;

	private String symptomCategory;
    
    private List<Syndrome> syndromes;

	public String getSymptomName() {
		return symptomName;
	}

	public void setSymptomName(String symptomName) {
		this.symptomName = symptomName;
	}

	public List<Syndrome> getSyndromes() {
		return syndromes;
	}

	public void setSyndromes(List<Syndrome> syndromes) {
		this.syndromes = syndromes;
	}

    public String getSymptomCategory() {
        return symptomCategory;
    }

    public void setSymptomCategory(String symptomCategory) {
        this.symptomCategory = symptomCategory;
    }
    
}