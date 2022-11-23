package io.lonmstalker.serialization.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class JavaNestedModel implements Serializable {
    private UUID v0;
    private String v1;
    private int v3;
}
