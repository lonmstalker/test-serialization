package io.lonmstalker.serialization.benchmark;

import io.lonmstalker.serialization.BenchmarkUtils;
import io.lonmstalker.serialization.TestUtils;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openjdk.jmh.annotations.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@ExtendWith(SpringExtension.class)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class DeserializationBenchmarkTest extends AbstractBenchmark {

  @Param({"5", "10", "50", "100"})
  public int countElements;

  @Benchmark
  public void bson_serialize() throws IOException {
    TestUtils.testDeserialization(countElements, BenchmarkUtils.BSON);
  }

  @Benchmark
  public void kryo_serialize() throws IOException {
    TestUtils.testDeserialization(countElements, BenchmarkUtils.KRYO);
  }

  @Benchmark
  public void one_nio_serialize() throws IOException {
    TestUtils.testDeserialization(countElements, BenchmarkUtils.ONE_NIO);
  }

  @Benchmark
  public void fst_serialize() throws IOException {
    TestUtils.testDeserialization(countElements, BenchmarkUtils.FST);
  }

  @Benchmark
  public void fst_unsafe_serialize() throws IOException {
    TestUtils.testDeserialization(countElements, BenchmarkUtils.UNSAFE_FST);
  }

  @Benchmark
  public void jackson_serialize() throws IOException {
    TestUtils.testDeserialization(countElements, BenchmarkUtils.JACKSON);
  }

  @Benchmark
  public void afterburner_jackson_serialize() throws IOException {
    TestUtils.testDeserialization(countElements, BenchmarkUtils.FULL_JACKSON);
  }

  @Benchmark
  public void smile_jackson_serialize() throws IOException {
    TestUtils.testDeserialization(countElements, BenchmarkUtils.SMILE_JACKSON);
  }
}
