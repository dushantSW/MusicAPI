package com.dushantsw.integration.entities;

import com.dushantsw.integration.entities.serializers.MBIDSerializer;
import com.dushantsw.integration.managers.exceptions.InvalidMBIDException;
import com.dushantsw.utilities.Commons;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

/**
 * <code>MBID</code> is a value-object defining the model for MBID. It also
 * implements validation.
 *
 * @author dushantsw
 */
@Data
@JsonSerialize(using = MBIDSerializer.class)
public class MBID {
    private String id;

    private MBID(String mbId) {
        this.id = mbId;
    }

    /**
     * Constructs a new instance of {@link MBID} if validation passes.
     *
     * @param id Unique string uuid.
     * @return {@link MBID}
     * @throws InvalidMBIDException if the {@code id} is empty, null or invalid.
     */
    public static MBID ofMbId(String id) throws InvalidMBIDException {
        if (id == null || id.isEmpty())
            throw new InvalidMBIDException("null or empty mbid");

        if (!Commons.isUUIDValid(id))
            throw new InvalidMBIDException("invalid mbid");

        return new MBID(id);
    }
}
