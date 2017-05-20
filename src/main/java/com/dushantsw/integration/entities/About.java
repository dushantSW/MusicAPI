package com.dushantsw.integration.entities;

import lombok.Builder;
import lombok.Data;

/**
 * <code>About</code>
 *
 * @author dushantsw
 */
@Data
@Builder
public class About {
    private String title;
    private String text;
}
