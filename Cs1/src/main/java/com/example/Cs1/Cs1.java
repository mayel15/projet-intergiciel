package com.example.Cs1;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Properties;

public class Cs1 {

    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String TOPIC_NAME = "topic1";
    private static final String GROUP_ID = "group1";
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5435/mirthdb";
    private static final String JDBC_USER = "mirthdb";
    private static final String JDBC_PASSWORD = "mirthdb";

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(TOPIC_NAME));

        DataParser dataParser = new DataParser();

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    String key = record.key();
                    String value = record.value();

                    // Parse the JSON data based on the key
                    switch (key) {
                        case "personsimple":
                            PersonSimpleData personSimpleData = dataParser.parsePersonSimpleData(value);
                            insertPersonSimple(personSimpleData);
                            break;
                        case "address":
                            AddressData addressData = dataParser.parseAddressData(value);
                            insertAddress(addressData);
                            break;
                        case "movements":
                            MovementsData movementsData = dataParser.parseMovementsData(value);
                            insertMovements(movementsData);
                            break;
                        case "stay":
                            StayData stayData = dataParser.parseStayData(value);
                            insertStay(stayData);
                            break;
                        default:
                            System.out.println("Unknown key: " + key);
                    }
                }
            }
        } finally {
            consumer.close();
        }
    }

    private static void insertPersonSimple(PersonSimpleData data) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String sql = "INSERT INTO personsimpleconsom (person_id, nom, prenom, naissance, sexe) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, data.getPerson_id());
            statement.setString(2, data.getNom());
            statement.setString(3, data.getPrenom());
            statement.setString(4, data.getNaissance());
            statement.setString(5, data.getSexe());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertAddress(AddressData data) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String sql = "INSERT INTO addressconsom (person_id, address, ville, code_postale, pays) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, data.getPerson_id());
            statement.setString(2, data.getAddress());
            statement.setString(3, data.getVille());
            statement.setString(4, data.getCode_postale());
            statement.setString(5, data.getPays());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertMovements(MovementsData data) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String sql = "INSERT INTO movementsconsom (person_id, numero_sejour, service, date_mouvement) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, data.getPerson_id());
            statement.setString(2, data.getNumero_sejour());
            statement.setString(3, data.getService());
            statement.setString(4, data.getDate_mouvement());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertStay(StayData data) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String sql = "INSERT INTO stayconsom (person_id, numero_sejour, date_debut, date_fin) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, data.getPerson_id());
            statement.setString(2, data.getNumero_sejour());
            statement.setString(3, data.getDate_debut());
            statement.setString(4, data.getDate_fin());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
