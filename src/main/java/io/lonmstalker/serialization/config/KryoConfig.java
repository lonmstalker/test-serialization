package io.lonmstalker.serialization.config;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import io.lonmstalker.serialization.model.JavaModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.serializer.Deserializer;
import org.springframework.core.serializer.Serializer;

import java.io.InputStream;

// https://www.baeldung.com/kryo
@Configuration(proxyBeanMethods = false)
public class KryoConfig {

  @Bean
  public Kryo kryo() {
    return new Kryo();
  }

  @Bean
  public Serializer<JavaModel> kryoSerializer(final Kryo kryo) {
    return (object, outputStream) -> kryo.writeClassAndObject(new Output(outputStream), object);
  }

  @Bean
  public Deserializer<JavaModel> kryoDeserializer(final Kryo kryo) {
    return new Deserializer<>() {
      @Override
      public JavaModel deserialize(InputStream inputStream) {
        return kryo.readObject(new Input(inputStream), JavaModel.class);
      }

      @Override
      public JavaModel deserializeFromByteArray(final byte[] serialized) {
        return kryo.readObject(new Input(serialized), JavaModel.class);
      }
    };
  }
}
