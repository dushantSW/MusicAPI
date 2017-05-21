package com.dushantsw.integration.entities.serializers;

import com.dushantsw.integration.entities.MBID;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * <code>MBIDSerializer</code>
 *
 * @author dushantsw
 */
public class MBIDSerializer extends JsonSerializer<MBID> {
    @Override
    public void serialize(MBID mbid, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException, JsonProcessingException {
        jsonGenerator.writeString(mbid.getId());
    }
}
