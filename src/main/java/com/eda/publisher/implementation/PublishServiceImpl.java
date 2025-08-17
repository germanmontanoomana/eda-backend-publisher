package com.eda.publisher.implementation;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eda.publisher.model.Document;
import com.eda.publisher.serializer.AvroSerializer;
import com.eda.publisher.service.PublishService;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;

@Service
public class PublishServiceImpl implements PublishService {
     @Autowired
     private PubSubTemplate pubSubTemplate;

     private static final String SCHEMA_DEFINITION = "{"
        + "  \"type\": \"record\","
        + "  \"name\": \"document\","
        + "  \"fields\": ["
        + "    {"
        + "      \"name\": \"fileName\","
        + "      \"type\": \"string\""
        + "    },"
        + "    {"
        + "      \"name\": \"fileContent\","
        + "      \"type\": \"string\""
        + "    }"
        + "  ]"
        + "}";

    private final AvroSerializer avroSerializer = new AvroSerializer();
    private static final Schema AVRO_SCHEMA = new Schema.Parser().parse(SCHEMA_DEFINITION);

    @Override
    public boolean publish() {
        try {
            // String uuid = UUID.randomUUID().toString();
            // Document document = new Document();
            // document.setFileName("fileName.png");
            // document.setFileContent("file content");

            // byte[] documentBytes = avroSerializer.serialize(document, AVRO_SCHEMA);
            // // Convert the byte array to a Pub/Sub ByteString
            // ByteString data = ByteString.copyFrom(documentBytes);

            // PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
            //     .setData(data)
            //     .build();


            // //System.out.println("Publishing message: " + messagePayload + " to topic: " + topicName);
            // this.pubSubTemplate.publish("projects/eda-processor/topics/documents.create", pubsubMessage);

            // Create a GenericRecord based on the schema
            GenericRecord avroRecord = new GenericData.Record(AVRO_SCHEMA);
            avroRecord.put("fileName", "fileName.png");
            avroRecord.put("fileContent", "fileContent");

            // Serialize the GenericRecord
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<>(AVRO_SCHEMA);
            Encoder encoder = EncoderFactory.get().binaryEncoder(outputStream, null);
            datumWriter.write(avroRecord, encoder);
            encoder.flush();

        // Publish the message
            ByteString data = ByteString.copyFrom(outputStream.toByteArray());
            PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
            .setData(data)
            .build();

            //this.pubSubTemplate.publish("projects/eda-processor/topics/documents.create", pubsubMessage);
            this.pubSubTemplate.publish("projects/eda-processor/topics/documents.test", "hola michi 2...");

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}