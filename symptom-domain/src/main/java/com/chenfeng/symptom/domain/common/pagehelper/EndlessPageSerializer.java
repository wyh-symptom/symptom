package com.chenfeng.symptom.domain.common.pagehelper;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class EndlessPageSerializer extends JsonSerializer<EndlessPage<?>> {  
    @Override  
    public void serialize(EndlessPage<?> value, JsonGenerator jgen, SerializerProvider provider)   
      throws IOException, JsonProcessingException {  
        jgen.writeStartObject();  
        jgen.writeBooleanField("lastPage", value.isLastPage());
        jgen.writeObjectField("content", value.getContent());
        jgen.writeEndObject();  
    }  
}  