package io.lonmstalker.serialization.config;

import io.lonmstalker.serialization.model.JavaModel;
import one.nio.serial.Json;
import one.nio.serial.JsonReader;
import one.nio.serial.PersistStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.serializer.Deserializer;
import org.springframework.core.serializer.Serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

// https://github.com/odnoklassniki/one-nio/blob/2d7fddc1e9af2738f08700c64909ecd3c350f8a3/test/one/nio/serial/SerializationTest.java#L46
@Configuration(proxyBeanMethods = false)
public class OneNioConfig {

  @Bean
  public Serializer<JavaModel> oneNioSerializer() {
    return new Serializer<>() {
      @Override
      public void serialize(final JavaModel object, final OutputStream outputStream)
          throws IOException {
        outputStream.write(Json.toJson(object).getBytes(StandardCharsets.UTF_8));
      }

      @Override
      public byte[] serializeToByteArray(final JavaModel object) throws IOException {
        return Json.toJson(object).getBytes(StandardCharsets.UTF_8);
      }
    };
  }

  @Bean
  public Deserializer<JavaModel> oneNioDeserializer() {
    return new Deserializer<>() {

      @Override
      public JavaModel deserialize(final InputStream inputStream) throws IOException {
        return this.read(inputStream.readAllBytes());
      }

      @Override
      public JavaModel deserializeFromByteArray(final byte[] serialized) throws IOException {
        return this.read(serialized);
      }

      private JavaModel read(final byte[] bytes) throws IOException {
        try {
          return new JsonReader(bytes).readObject(JavaModel.class);
        } catch (ClassNotFoundException ex) {
          return null;
        }
      }
    };
  }
}
