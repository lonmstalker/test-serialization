package io.lonmstalker.serialization.config;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferInput;
import com.esotericsoftware.kryo.io.ByteBufferOutput;
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
    return new Serializer<>() {
      @Override
      public void serialize(JavaModel object, OutputStream outputStream) {
        Kryo kryoObtain = null;
        try (final var buffer = new ByteBufferOutput(outputStream)) {
          kryoObtain = kryo.obtain();
          kryoObtain.writeClassAndObject(buffer, object);
        } finally {
          kryo.free(kryoObtain);
        }
      }

      @Override
      public byte[] serializeToByteArray(final JavaModel object) {
        Kryo kryoObtain = null;
        try (final var buffer = new ByteBufferOutput(4096, -1)) {
          kryoObtain = kryo.obtain();
          kryoObtain.writeObjectOrNull(buffer, object, JavaModel.class);
          return buffer.toBytes();
        } finally {
          kryo.free(kryoObtain);
        }
      }
    };
  }

  @Bean
  public Deserializer<JavaModel> kryoDeserializer(final Pool<Kryo> kryo) {
    return new Deserializer<>() {
      @Override
      public JavaModel deserialize(InputStream inputStream) {
        Kryo kryoObtain = null;
        try {
          kryoObtain = kryo.obtain();
          return kryoObtain.readObjectOrNull(new ByteBufferInput(inputStream), JavaModel.class);
        } finally {
          kryo.free(kryoObtain);
        }
      }

      @Override
      public JavaModel deserializeFromByteArray(final byte[] serialized) {
        Kryo kryoObtain = null;
        try {
          kryoObtain = kryo.obtain();
          return kryoObtain.readObjectOrNull(new ByteBufferInput(serialized), JavaModel.class);
        } finally {
          kryo.free(kryoObtain);
        }
      }
    };
  }
}
