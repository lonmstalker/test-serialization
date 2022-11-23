package io.lonmstalker.serialization.config;

import io.lonmstalker.serialization.model.JavaModel;
import org.bson.BsonType;
import org.bson.UuidRepresentation;
import org.bson.codecs.*;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.internal.ProvidersCodecRegistry;
import org.bson.json.JsonReader;
import org.bson.json.JsonWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.serializer.Deserializer;
import org.springframework.core.serializer.Serializer;

import java.io.*;
import java.util.List;

// https://www.baeldung.com/mongodb-bson
@Configuration(proxyBeanMethods = false)
public class BsonConfig {

  @Bean
  public Codec<JavaModel> codecProvider() {
    final var provider =
        PojoCodecProvider.builder()
            .automatic(true)
            .register(JavaModel.class, Long.class, Boolean.class, byte.class, String.class)
            .automatic(true)
            .conventions(Conventions.DEFAULT_CONVENTIONS)
            .build();
    final var providersCodecRegistry =
        new ProvidersCodecRegistry(
            List.of(
                provider,
                new ValueCodecProvider(),
                new UuidCodecProvider(UuidRepresentation.STANDARD)));
    return provider.get(JavaModel.class, providersCodecRegistry);
  }

  @Bean
  public Deserializer<JavaModel> bsonDeserializer(final Codec<JavaModel> codec) {
    return new Deserializer<>() {

      @Override
      public JavaModel deserialize(final InputStream inputStream) {
        final var jsonReader = new JsonReader(new InputStreamReader(inputStream));
        return jsonReader.readBsonType() == BsonType.NULL
            ? null
            : codec.decode(jsonReader, DecoderContext.builder().build());
      }

      @Override
      public JavaModel deserializeFromByteArray(final byte[] serialized) {
        final var jsonReader =
            new JsonReader(new InputStreamReader(new ByteArrayInputStream(serialized)));
        return jsonReader.readBsonType() == BsonType.NULL
            ? null
            : codec.decode(jsonReader, DecoderContext.builder().build());
      }
    };
  }

  @Bean
  public Serializer<JavaModel> bsonSerializer(final Codec<JavaModel> codec) {
    return (object, outputStream) ->
        codec.encode(
            new JsonWriter(new OutputStreamWriter(outputStream)),
            object,
            EncoderContext.builder().build());
  }
}
