package io.lonmstalker.serialization.benchmark;

import org.junit.Test;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

public abstract class AbstractBenchmark {

  @Test
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
