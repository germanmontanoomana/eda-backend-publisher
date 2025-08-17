package com.eda.publisher.serializer;

import org.apache.avro.Schema;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AvroSerializer {
    public byte[] serialize(Object avroObject, Schema avroSchema) throws IOException {
        DatumWriter<Object> writer = new SpecificDatumWriter<>(avroSchema);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Encoder encoder = EncoderFactory.get().binaryEncoder(outputStream, null);
        writer.write(avroObject, encoder);
        encoder.flush();
        return outputStream.toByteArray();
    }
}