package com.dushantsw.integration.entities.serializers;

import com.dushantsw.integration.entities.AlbumTitle;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * <code>AlbumTitleSerializer</code>
 *
 * @author dushantsw
 */
public class AlbumTitleSerializer extends JsonSerializer<AlbumTitle> {
    @Override
    public void serialize(AlbumTitle albumTitle, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException, JsonProcessingException {
        jsonGenerator.writeString(albumTitle.getTitle());
    }
}
