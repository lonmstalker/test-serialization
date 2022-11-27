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
public class SerializationBenchmarkTest extends AbstractBenchmark {

  @Param({"5"})
  public int countElements;

  @Benchmark
  public void kryo_serialize() throws IOException {
    TestUtils.testSerialization(countElements, BenchmarkUtils.KRYO.getKey());
  }

  @Benchmark
  public void one_nio_serialize() throws IOException {
    TestUtils.testSerialization(countElements, BenchmarkUtils.ONE_NIO.getKey());
  }

  @Benchmark
  public void fst_serialize() throws IOException {
    TestUtils.testSerialization(countElements, BenchmarkUtils.FST.getKey());
  }

  @Benchmark
  public void fst_unsafe_serialize() throws IOException {
    TestUtils.testSerialization(countElements, BenchmarkUtils.UNSAFE_FST.getKey());
  }

  @Benchmark
  public void jackson_serialize() throws IOException {
    TestUtils.testSerialization(countElements, BenchmarkUtils.JACKSON.getKey());
  }

  @Benchmark
  public void afterburner_jackson_serialize() throws IOException {
    TestUtils.testSerialization(countElements, BenchmarkUtils.FULL_JACKSON.getKey());
  }

  @Benchmark
  public void smile_jackson_serialize() throws IOException {
    TestUtils.testSerialization(countElements, BenchmarkUtils.SMILE_JACKSON.getKey());
  }
}
