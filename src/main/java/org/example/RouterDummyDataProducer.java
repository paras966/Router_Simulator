package org.example;

import java.io.*;
import java.util.Properties;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class RouterDummyDataProducer {
    public static void main(String[] args) throws InterruptedException, IOException {
        String csvFile = "src/main/resources/routersData.csv";
        String kafkaTopic = "routersData";

        // SET Producer configuration
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        properties.put(ProducerConfig.RETRIES_CONFIG, 3);
        properties.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 30000);
        properties.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 45000);
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        properties.put(ProducerConfig.LINGER_MS_CONFIG, 1);

        // CREATE Instance of kafka Producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        try (CSVParser csvParser = new CSVParser(new FileReader(csvFile), CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
            for (CSVRecord record : csvParser) {
                String key = record.get("Timestamp");
                // Convert CSV record to String
                String value = record.toMap().toString();
                producer.send(new ProducerRecord<>(kafkaTopic, key, value));
                // Sleep for 2 second
                Thread.sleep(2000);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            producer.flush();
            producer.close();
        }
    }
}