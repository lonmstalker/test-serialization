package io.lonmstalker.serialization.benchmark;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.io.IOException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class AbstractBenchmark {

  @Test
  @Order(1)
  public void validate_serialize() throws IOException {
    final var serializeTest = new SerializationBenchmarkTest();
    serializeTest.countElements = 1;

    serializeTest.jackson_serialize();
    serializeTest.smile_jackson_serialize();
    serializeTest.afterburner_jackson_serialize();

    serializeTest.kryo_serialize();
    serializeTest.one_nio_serialize();

    serializeTest.fst_serialize();
    serializeTest.fst_unsafe_serialize();
  }

  @Test
  @Order(2)
  public void validate_deserialize() throws IOException {
    final var serializeTest = new DeserializationBenchmarkTest();
    serializeTest.countElements = 1;

    serializeTest.jackson_deserialize();
    serializeTest.smile_jackson_deserialize();
    serializeTest.afterburner_jackson_deserialize();

    serializeTest.kryo_deserialize();
    serializeTest.one_nio_deserialize();

    serializeTest.fst_deserialize();
    serializeTest.fst_unsafe_deserialize();
  }

  @Test
  @Order(3)
  public void execute_jmh() throws RunnerException {
    final var opt =
        new OptionsBuilder()
            .include("\\." + this.getClass().getSimpleName() + "\\.")
            .warmupTime(TimeValue.seconds(1))
            .warmupIterations(3)
            .measurementIterations(3)
            .forks(1)
            .threads(1)
            .shouldDoGC(true)
            .shouldFailOnError(true)
            .build();
    new Runner(opt).run();
  }
}
