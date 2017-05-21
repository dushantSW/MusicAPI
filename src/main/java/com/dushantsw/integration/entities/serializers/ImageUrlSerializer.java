package com.dushantsw.integration.entities.serializers;

import com.dushantsw.integration.entities.ImageUrl;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * <code>ImageUrlSerializer</code>
 *
 * @author dushantsw
 */
public class ImageUrlSerializer extends JsonSerializer<ImageUrl> {
    @Override
    public void serialize(ImageUrl imageUrl, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException, JsonProcessingException {
        jsonGenerator.writeString(imageUrl.getUrl());
    }
}
