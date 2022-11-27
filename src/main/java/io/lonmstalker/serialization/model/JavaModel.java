package io.lonmstalker.serialization.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@Jacksonized
@AllArgsConstructor
public class JavaModel implements Serializable {
  private UUID v0;
  private String v1;
  private int v2;
  private List<String> v3;
  private JavaNestedModel v4;
}
