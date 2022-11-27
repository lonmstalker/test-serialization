package io.lonmstalker.serialization;

import io.lonmstalker.serialization.config.*;
import io.lonmstalker.serialization.model.JavaModel;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.serializer.Deserializer;
import org.springframework.core.serializer.Serializer;

@UtilityClass
public class BenchmarkUtils {

  public final Pair<Serializer<JavaModel>, Deserializer<JavaModel>> FST =
      BenchmarkUtils.createFst();

  public final Pair<Serializer<JavaModel>, Deserializer<JavaModel>> UNSAFE_FST =
      BenchmarkUtils.createUnsafeFst();

  public final Pair<Serializer<JavaModel>, Deserializer<JavaModel>> JACKSON =
      BenchmarkUtils.createJackson();

  public final Pair<Serializer<JavaModel>, Deserializer<JavaModel>> FULL_JACKSON =
      BenchmarkUtils.createAfterburnerJackson();

  public final Pair<Serializer<JavaModel>, Deserializer<JavaModel>> SMILE_JACKSON =
      BenchmarkUtils.createSmileJackson();

  public final Pair<Serializer<JavaModel>, Deserializer<JavaModel>> KRYO =
      BenchmarkUtils.createKryo();

  public final Pair<Serializer<JavaModel>, Deserializer<JavaModel>> ONE_NIO =
      BenchmarkUtils.createOneNio();

  public Pair<Serializer<JavaModel>, Deserializer<JavaModel>> createFst() {
    final var fstConfig = new FstConfig();
    final var fstConfiguration = fstConfig.fstConfiguration();
    return Pair.of(
        fstConfig.createFstSerializer(fstConfiguration),
        fstConfig.createFstDeserializer(fstConfiguration));
  }

  public Pair<Serializer<JavaModel>, Deserializer<JavaModel>> createUnsafeFst() {
    final var fstConfig = new FstConfig();
    final var fstConfiguration = fstConfig.unsafeFstConfiguration();
    return Pair.of(
        fstConfig.createFstSerializer(fstConfiguration),
        fstConfig.createFstDeserializer(fstConfiguration));
  }

  public Pair<Serializer<JavaModel>, Deserializer<JavaModel>> createJackson() {
    final var jacksonConfig = new JacksonConfig();
    final var objectMapper = jacksonConfig.objectMapper();
    return Pair.of(
        jacksonConfig.jacksonSerializer(objectMapper),
        jacksonConfig.jacksonDeserializer(objectMapper));
  }

  public Pair<Serializer<JavaModel>, Deserializer<JavaModel>> createAfterburnerJackson() {
    final var jacksonConfig = new JacksonConfig();
    final var objectMapper = jacksonConfig.fullObjectMapper();
    return Pair.of(
        jacksonConfig.jacksonSerializer(objectMapper),
        jacksonConfig.jacksonDeserializer(objectMapper));
  }

  public Pair<Serializer<JavaModel>, Deserializer<JavaModel>> createSmileJackson() {
    final var jacksonConfig = new JacksonConfig();
    final var objectMapper = jacksonConfig.smileObjectMapper();
    return Pair.of(
        jacksonConfig.jacksonSerializer(objectMapper),
        jacksonConfig.jacksonDeserializer(objectMapper));
  }

  public Pair<Serializer<JavaModel>, Deserializer<JavaModel>> createKryo() {
    final var kryoConfig = new KryoConfig();
    final var kryo = kryoConfig.kryo();
    return Pair.of(kryoConfig.kryoSerializer(kryo), kryoConfig.kryoDeserializer(kryo));
  }

  public Pair<Serializer<JavaModel>, Deserializer<JavaModel>> createOneNio() {
    final var oneNioConfig = new OneNioConfig();
    return Pair.of(oneNioConfig.oneNioSerializer(), oneNioConfig.oneNioDeserializer());
  }
}
