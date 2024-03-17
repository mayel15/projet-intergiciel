package com.pr1.demo;

import com.google.gson.JsonObject;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class Pr1 {
    private static final String TOPIC_NAME = "topic1";
    private static String BROKER_URL = "localhost"; // L'adresse IP du broker Kafka
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5435/mirthdb";
    private static final String JDBC_USER = "mirthdb";
    private static final String JDBC_PASSWORD = "mirthdb";

    public static void main(String[] args) {
        try {
            // Obtention de l'adresse IP de la machine
            InetAddress localhost = InetAddress.getLocalHost();
            BROKER_URL = localhost.getHostAddress() + ":9092"; // Utiliser le port par défaut de Kafka

            // Configuration des propriétés du producteur Kafka
            Properties props = new Properties();
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_URL);
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

            Connection conn = null;
            Statement stmt = null;

            try {
                // Création d'une instance de producteur Kafka
                Producer<String, String> producer = new org.apache.kafka.clients.producer.KafkaProducer<>(props);

                while (true) { // Boucle infinie
                    try {
                        // Connexion à la base de données PostgreSQL de Mirth Connect
                        conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                        stmt = conn.createStatement();

                        // Table personSimple
                        String sqlPersonSimple = "SELECT person_id, nom, prenom, naissance, sexe FROM public.personSimple";
                        ResultSet rsPersonSimple = stmt.executeQuery(sqlPersonSimple);
                        while (rsPersonSimple.next()) {
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty("person_id", rsPersonSimple.getString("person_id"));
                            jsonObject.addProperty("nom", rsPersonSimple.getString("nom"));
                            jsonObject.addProperty("prenom", rsPersonSimple.getString("prenom"));
                            jsonObject.addProperty("sexe", rsPersonSimple.getString("sexe"));
                            jsonObject.addProperty("naissance", rsPersonSimple.getString("naissance"));
                            producer.send(new ProducerRecord<>(TOPIC_NAME, "personsimple", jsonObject.toString()));
                            System.out.println("Message envoyé au topic Kafka (PersonSimple) : " + jsonObject);
                        }
                        rsPersonSimple.close();

                        // Table Address
                        String sqlAddress = "SELECT person_id, address, ville, code_postale, pays FROM public.Address";
                        ResultSet rsAddress = stmt.executeQuery(sqlAddress);
                        while (rsAddress.next()) {
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty("person_id", rsAddress.getString("person_id"));
                            jsonObject.addProperty("address", rsAddress.getString("address"));
                            jsonObject.addProperty("ville", rsAddress.getString("ville"));
                            jsonObject.addProperty("code_postale", rsAddress.getString("code_postale"));
                            jsonObject.addProperty("pays", rsAddress.getString("pays"));
                            producer.send(new ProducerRecord<>(TOPIC_NAME, "address", jsonObject.toString()));
                            System.out.println("Message envoyé au topic Kafka (Address) : " + jsonObject);
                        }
                        rsAddress.close();

                        // Table Movements
                        String sqlMovements = "SELECT person_id, numero_sejour, service, date_mouvement FROM public.Movements";
                        ResultSet rsMovements = stmt.executeQuery(sqlMovements);
                        while (rsMovements.next()) {
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty("person_id", rsMovements.getString("person_id"));
                            jsonObject.addProperty("numero_sejour", rsMovements.getString("numero_sejour"));
                            jsonObject.addProperty("service", rsMovements.getString("service"));
                            jsonObject.addProperty("date_mouvement", rsMovements.getString("date_mouvement"));
                            producer.send(new ProducerRecord<>(TOPIC_NAME, "movements", jsonObject.toString()));
                            System.out.println("Message envoyé au topic Kafka (Movements) : " + jsonObject);
                        }
                        rsMovements.close();

                        // Table Stay
                        String sqlStay = "SELECT person_id, numero_sejour, date_debut, date_fin FROM public.Stay";
                        ResultSet rsStay = stmt.executeQuery(sqlStay);
                        while (rsStay.next()) {
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty("person_id", rsStay.getString("person_id"));
                            jsonObject.addProperty("numero_sejour", rsStay.getString("numero_sejour"));
                            jsonObject.addProperty("date_debut", rsStay.getString("date_debut"));
                            jsonObject.addProperty("date_fin", rsStay.getString("date_fin"));
                            producer.send(new ProducerRecord<>(TOPIC_NAME, "stay", jsonObject.toString()));
                            System.out.println("Message envoyé au topic Kafka (Stay) : " + jsonObject);
                        }
                        rsStay.close();

                        // Fermeture de la connexion à la base de données
                        stmt.close();
                        conn.close();

                        // Attendre pendant un certain temps avant de vérifier à nouveau la base de données
                        Thread.sleep(5000); // Attendre 5 secondes avant de vérifier à nouveau
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        // Fermeture de la connexion à la base de données
                        if (stmt != null) stmt.close();
                        if (conn != null) conn.close();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
