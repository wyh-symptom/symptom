package com.chenfeng.symptom.domain.common.serializer;

import java.io.IOException;

import com.chenfeng.symptom.domain.common.pagehelper.CountablePage;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CountablePageSerializer extends JsonSerializer<CountablePage<?>> {  
    @Override  
    public void serialize(CountablePage<?> value, JsonGenerator jgen, SerializerProvider provider)   
      throws IOException, JsonProcessingException {  
        jgen.writeStartObject();  
        jgen.writeNumberField("total", value.getTotal());
        jgen.writeNumberField("currentPage", value.getPageNum());
        jgen.writeNumberField("totalPages", value.getPages());
        jgen.writeObjectField("content", value.getContent());
        jgen.writeEndObject();  
    }  
}  