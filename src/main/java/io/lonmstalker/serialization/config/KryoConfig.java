package io.lonmstalker.serialization.config;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferInput;
import com.esotericsoftware.kryo.unsafe.UnsafeByteBufferOutput;
import com.esotericsoftware.kryo.util.DefaultInstantiatorStrategy;
import com.esotericsoftware.kryo.util.Pool;
import io.lonmstalker.serialization.model.JavaModel;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.serializer.Deserializer;
import org.springframework.core.serializer.Serializer;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

// kryo not thread safe, in real project use ThreadLocal
// --add-opens java.base/java.util=ALL-UNNAMED
// --add-opens java.base/sun.nio.ch=ALL-UNNAMED
// for unsafe: --add-opens=java.base/java.nio=ALL-UNNAMED
// https://www.baeldung.com/kryo
@Configuration(proxyBeanMethods = false)
public class KryoConfig {

  // can replace with new KryoPool.Builder(factory).softReferences().build();
  @Bean
  public Pool<Kryo> kryo() {
    return new Pool<>(true, false, Runtime.getRuntime().availableProcessors()) {
      @Override
      protected Kryo create() {
        final var kryo = new Kryo();
        kryo.register(JavaModel.class);
        kryo.setRegistrationRequired(false);
        kryo.setInstantiatorStrategy(
            new DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
        return kryo;
      }
    };
  }

  @Bean
  public Serializer<JavaModel> kryoSerializer(final Pool<Kryo> kryo) {
    var output =
        new Pool<UnsafeByteBufferOutput>(true, false, Runtime.getRuntime().availableProcessors()) {
          @Override
          protected UnsafeByteBufferOutput create() {
            return new UnsafeByteBufferOutput(1024, -1);
          }
        };
    return new Serializer<>() {
      @Override
      public void serialize(JavaModel object, OutputStream outputStream) {
        Kryo kryoObtain = null;
        UnsafeByteBufferOutput outputObtain = null;
        try {
          kryoObtain = kryo.obtain();
          outputObtain = output.obtain();

          outputObtain.reset();
          outputObtain.setOutputStream(outputStream);
          kryoObtain.writeClassAndObject(outputObtain, object);
        } finally {
          kryo.free(kryoObtain);
          output.free(outputObtain);
        }
      }

      @Override
      public byte[] serializeToByteArray(JavaModel object) {
        Kryo kryoObtain = null;
        UnsafeByteBufferOutput outputObtain = null;
        try {
          kryoObtain = kryo.obtain();
          outputObtain = output.obtain();

          outputObtain.reset();
          kryoObtain.writeClassAndObject(outputObtain, object);
          return outputObtain.toBytes();
        } finally {
          kryo.free(kryoObtain);
          output.free(outputObtain);
        }
      }
    };
  }

  @Bean
  public Deserializer<JavaModel> kryoDeserializer(final Pool<Kryo> kryo) {
    var input =
        new Pool<ByteBufferInput>(true, false, Runtime.getRuntime().availableProcessors()) {
          @Override
          protected ByteBufferInput create() {
            return new ByteBufferInput(1024);
          }
        };
    return new Deserializer<>() {
      @Override
      public JavaModel deserialize(InputStream inputStream) {
        Kryo kryoObtain = null;
        ByteBufferInput inputObtain = null;
        try {
          kryoObtain = kryo.obtain();
          inputObtain = input.obtain();
          inputObtain.setInputStream(inputStream);
          return (JavaModel) kryoObtain.readClassAndObject(inputObtain);
        } finally {
          kryo.free(kryoObtain);
          input.free(inputObtain);
        }
      }

      @Override
      public JavaModel deserializeFromByteArray(final byte[] serialized) {
        Kryo kryoObtain = null;
        ByteBufferInput inputObtain = null;
        try {
          final var buffer = ByteBuffer.allocateDirect(serialized.length);

          kryoObtain = kryo.obtain();
          inputObtain = input.obtain();
          buffer.put(serialized);

          inputObtain.setBuffer(buffer);
          return (JavaModel) kryoObtain.readClassAndObject(inputObtain);
        } finally {
          kryo.free(kryoObtain);
          input.free(inputObtain);
        }
      }
    };
  }
}
