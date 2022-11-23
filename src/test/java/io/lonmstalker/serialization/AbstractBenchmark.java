package io.lonmstalker.serialization;

import org.junit.Test;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public abstract class AbstractBenchmark {

  @Test
  public void execute_jmh() throws RunnerException {
    final var opt =
        new OptionsBuilder()
            .include("\\." + this.getClass().getSimpleName() + "\\.")
            .warmupIterations(2)
            .measurementIterations(3)
            .forks(0)
            .threads(1)
            .shouldDoGC(true)
            .shouldFailOnError(true)
            .resultFormat(ResultFormatType.JSON)
            .result("/dev/null")
            .build();
    new Runner(opt).run();
  }
}
