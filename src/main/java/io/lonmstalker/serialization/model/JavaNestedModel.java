package io.lonmstalker.serialization.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@Jacksonized
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.USE_DEFAULTS)
@JsonIgnoreProperties(ignoreUnknown = true)
public class JavaNestedModel implements Serializable {
  private UUID v0;
  private String v1;
  private int v3;
}
