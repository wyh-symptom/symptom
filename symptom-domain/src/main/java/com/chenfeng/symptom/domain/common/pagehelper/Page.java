package com.chenfeng.symptom.domain.common.pagehelper;

import java.util.List;

public interface Page<E> extends List<E>{

	public List<E> getContent() ;
	
	public boolean isLastPage() ;

}
