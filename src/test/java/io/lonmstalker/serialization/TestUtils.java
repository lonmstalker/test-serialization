package io.lonmstalker.serialization;

import io.lonmstalker.serialization.model.JavaModel;
import io.lonmstalker.serialization.model.JavaNestedModel;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.springframework.core.serializer.Deserializer;
import org.springframework.core.serializer.Serializer;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@UtilityClass
public class TestUtils {
  private final JavaModel TEST_MODEL = createModel();

  public void testSerialization(final int countElements, final Serializer<JavaModel> serializer)
      throws IOException {
    for (int i = 0; i < countElements; i++) {
      Assertions.assertNotNull(serializer.serializeToByteArray(TEST_MODEL));
    }
  }

  public void testDeserialization(
      final int countElements, final Pair<Serializer<JavaModel>, Deserializer<JavaModel>> pair)
      throws IOException {
    final var deserializer = pair.getValue();
    final var data = pair.getKey().serializeToByteArray(TEST_MODEL);
    for (int i = 0; i < countElements; i++) {
      Assertions.assertNotNull(deserializer.deserializeFromByteArray(data));
    }
  }

  public JavaModel createModel() {
    return JavaModel.builder()
        .v0(UUID.randomUUID())
        .v1(RandomStringUtils.random(10, true, true))
        .v2(ThreadLocalRandom.current().nextInt())
        .v3(IntStream.range(10, 15).mapToObj(i -> RandomStringUtils.random(i, true, true)).toList())
        .v4(
            JavaNestedModel.builder()
                .v0(UUID.randomUUID())
                .v1(RandomStringUtils.random(10, true, true))
                .v3(ThreadLocalRandom.current().nextInt())
                .build())
        .build();
  }
}
