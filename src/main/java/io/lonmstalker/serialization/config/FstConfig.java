package io.lonmstalker.serialization.config;

import io.lonmstalker.serialization.model.JavaModel;
import org.nustaq.serialization.FSTConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.serializer.Deserializer;
import org.springframework.core.serializer.Serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

// models must be serializable
// need use --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.math=ALL-UNNAMED
// --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/java.util.concurrent=ALL-UNNAMED
// --add-opens java.base/java.net=ALL-UNNAMED --add-opens java.base/java.text=ALL-UNNAMED
// --add-opens java.sql/java.sql=ALL-UNNAMED

// https://www.alibabacloud.com/blog/an-introduction-and-comparison-of-several-common-java-serialization-frameworks_597900
@Configuration(proxyBeanMethods = false)
public class FstConfig {

  @Bean
  public FSTConfiguration fstConfiguration() {
    final var config = FSTConfiguration.createJsonConfiguration();
    config.registerClass(JavaModel.class);
    config.setShareReferences(false);
    return config;
  }

  @Bean
  public FSTConfiguration unsafeFstConfiguration() {
    final var config = FSTConfiguration.createUnsafeBinaryConfiguration();
    config.registerClass(JavaModel.class);
    config.setShareReferences(false);
    return config;
  }

  @Bean
  public Serializer<JavaModel> fstSerializer(
      @Qualifier("fstConfiguration") final FSTConfiguration fstConfiguration,
      @Qualifier("unsafeFstConfiguration") final FSTConfiguration unsafeFstConfiguration) {
    return new Serializer<>() {
      @Override
      public void serialize(final JavaModel object, final OutputStream outputStream)
          throws IOException {
        fstConfiguration.encodeToStream(outputStream, object);
      }

      @Override
      public byte[] serializeToByteArray(final JavaModel object) {
        return unsafeFstConfiguration.asByteArray(object);
      }
    };
  }

  @Bean
  public Deserializer<JavaModel> fstDeserializer(
      @Qualifier("fstConfiguration") final FSTConfiguration fstConfiguration,
      @Qualifier("unsafeFstConfiguration") final FSTConfiguration unsafeFstConfiguration) {
    return new Deserializer<>() {

      @Override
      public JavaModel deserialize(final InputStream inputStream) {
        try {
          return (JavaModel) fstConfiguration.decodeFromStream(inputStream);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }

      @Override
      public JavaModel deserializeFromByteArray(final byte[] serialized) {
        try {
          return (JavaModel) unsafeFstConfiguration.asObject(serialized);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    };
  }
}