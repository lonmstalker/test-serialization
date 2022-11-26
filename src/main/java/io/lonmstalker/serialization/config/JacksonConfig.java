package io.lonmstalker.serialization.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;
import io.lonmstalker.serialization.model.JavaModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.serializer.Deserializer;
import org.springframework.core.serializer.Serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Configuration(proxyBeanMethods = false)
public class JacksonConfig {

  @Bean("fullObjectMapper")
  public ObjectMapper fullObjectMapper() {
    return new ObjectMapper().findAndRegisterModules();
  }

  @Bean("objectMapper")
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

  @Bean("smileObjectMapper")
  public ObjectMapper smileObjectMapper() {
    return new ObjectMapper(new SmileFactory()).findAndRegisterModules();
  }

  @Bean
  public Serializer<JavaModel> jacksonSerializer(
      @Qualifier("objectMapper") final ObjectMapper objectMapper) {
    return createSerializer(objectMapper);
  }

  @Bean
  public Serializer<JavaModel> jacksonAfterburnerSerializer(
      @Qualifier("fullObjectMapper") final ObjectMapper objectMapper) {
    return createSerializer(objectMapper);
  }

  @Bean
  public Serializer<JavaModel> jacksonSmileSerializer(
      @Qualifier("smileObjectMapper") final ObjectMapper objectMapper) {
    return createSerializer(objectMapper);
  }

  @Bean
  public Deserializer<JavaModel> jacksonDeserializer(
      @Qualifier("objectMapper") final ObjectMapper objectMapper) {
    return createDeserializer(objectMapper);
  }

  @Bean
  public Deserializer<JavaModel> jacksonSmileDeserializer(
      @Qualifier("smileObjectMapper") final ObjectMapper objectMapper) {
    return createDeserializer(objectMapper);
  }

  @Bean
  public Deserializer<JavaModel> jacksonAfterburnerDeserializer(
      @Qualifier("fullObjectMapper") final ObjectMapper objectMapper) {
    return createDeserializer(objectMapper);
  }

  private Deserializer<JavaModel> createDeserializer(final ObjectMapper objectMapper) {
    return new Deserializer<>() {

      @Override
      public JavaModel deserialize(final InputStream inputStream) throws IOException {
        return objectMapper.readValue(inputStream, JavaModel.class);
      }

      @Override
      public JavaModel deserializeFromByteArray(final byte[] serialized) throws IOException {
        return objectMapper.readValue(serialized, JavaModel.class);
      }
    };
  }

  private Serializer<JavaModel> createSerializer(final ObjectMapper objectMapper) {
    return new Serializer<>() {

      @Override
      public void serialize(final JavaModel object, final OutputStream outputStream)
          throws IOException {
        objectMapper.writeValue(outputStream, object);
      }

      @Override
      public byte[] serializeToByteArray(final JavaModel object) throws IOException {
        return objectMapper.writeValueAsBytes(object);
      }
    };
  }
}
