package io.lonmstalker.serialization.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class JavaModel implements Serializable {
    private UUID v0;
    private String v1;
    private int v2;
    private Collection<String> v3;
    private JavaNestedModel v4;
}
