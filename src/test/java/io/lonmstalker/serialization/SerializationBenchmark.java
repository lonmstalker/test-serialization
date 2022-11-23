package io.lonmstalker.serialization;

import io.lonmstalker.serialization.config.BsonConfig;
import io.lonmstalker.serialization.model.JavaModel;
import io.lonmstalker.serialization.model.JavaNestedModel;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openjdk.jmh.annotations.*;
import org.springframework.core.serializer.Deserializer;
import org.springframework.core.serializer.Serializer;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@ExtendWith(SpringExtension.class)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class SerializationBenchmark extends AbstractBenchmark {

  @Param({"10", "100", "1000", "10000"})
  public int countElements;

  private static final Pair<Serializer<JavaModel>, Deserializer<JavaModel>> BSON = createBson();

  @Benchmark
  public void bson_serialize() throws IOException {
    final var serializer = BSON.getKey();
    for (int i = 0; i < countElements; i++) {
      serializer.serializeToByteArray(this.createModel());
    }
  }

  private JavaModel createModel() {
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

  private static Pair<Serializer<JavaModel>, Deserializer<JavaModel>> createBson() {
    final var bsonConfig = new BsonConfig();
    final var bsonCodec = bsonConfig.codecProvider();
    return Pair.of(bsonConfig.bsonSerializer(bsonCodec), bsonConfig.bsonDeserializer(bsonCodec));
  }
}
