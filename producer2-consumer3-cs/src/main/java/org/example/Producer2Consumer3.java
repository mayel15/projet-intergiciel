package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Producer2Consumer3 {
    Scanner scanner = new Scanner(System.in);
    private static final String TOPIC_2 = "topic2";
    private static final String TOPIC_3 = "topic3";
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String GROUP_ID = "group3";

    private Producer<String, String> producer;
    private Consumer<String, ArrayList<String>> consumer;

    public Producer2Consumer3() {
        initializeProducer();
        initializeConsumer();
    }

    private void initializeProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producer = new org.apache.kafka.clients.producer.KafkaProducer<>(props);
    }

    private void initializeConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ArrayListDeserializer.class.getName());
        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(TOPIC_3));
    }

    public void start() {
        try {
            while (true) {
                ArrayList<String> keyValue = handleConsoleInput();
                String commandText = "";
                String commandValue = "";
                if (keyValue.size() > 0) {
                    commandText = keyValue.get(0);
                    commandValue = keyValue.get(1);
                }

                //System.out.println("keyValue: " + keyValue);
                producer.send(new ProducerRecord<>(TOPIC_2, commandText, commandValue));
                ConsumerRecords<String, ArrayList<String>> records = consumer.poll(100000);
                records.forEach(record -> {
                    String key = record.key();

                    ArrayList<String> value = record.value();
                    //System.out.println("key: "+key+"; value: "+value));
                    try {
                        if (key.equals("export")) {
                            exportJson(value);
                        } else {
                            for (String data: value) {
                                System.out.println(data);
                            }
                        }

                        consumer.commitSync();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } finally {
            producer.close();
            consumer.close();
        }
    }

    // gere les commande envoyé depuis la console par le user
    private ArrayList<String> handleConsoleInput() {
        String command;
        String value;
        ArrayList<String> keyValue = new ArrayList<String>();
        System.out.println("Bienvenue dans la console texte!");
        System.out.print("Entrez une commande (ou 'exit' pour quitter): ");
        command = scanner.nextLine();

        // Executer les actions appropriées en fonction de la commande saisie
        switch (command) {
            case "get_all_patients":
                keyValue.add(command);
                value = "all";
                keyValue.add(value);
                break;
            case "get_patient_by_pid":
                keyValue.add(command);
                System.out.print("Entrez le pid du patient: ");
                value = scanner.nextLine();
                keyValue.add(value);
                break;
            case "get_patient_by_name":
                keyValue.add(command);
                System.out.print("Entrez le nom du patient: ");
                value = scanner.nextLine();
                keyValue.add(value);
                break;
            case "get_patient_stay_by_pid":
                keyValue.add(command);
                System.out.print("Entrez le pid du patient: ");
                value = scanner.nextLine();
                keyValue.add(value);
                break;
            case "get_patient_movements_by_sid":
                keyValue.add(command);
                System.out.print("Entrez le numero_sejour du patient: ");
                value = scanner.nextLine();
                keyValue.add(value);
                break;
            case "export":
                keyValue.add(command);
                value = "export";
                keyValue.add(value);
                break;
            case "help":
                keyValue.add(command);
                keyValue.add("help");
                break;
            case "exit":
                keyValue.add(command);
                keyValue.add("exit");
                break;
            default:
                keyValue.add(command);
                keyValue.add("undefined command");
        }
        return keyValue;
    }

    // export de la database en json
    private static void exportJson(List<String> data) {
        String filePath = "src/main/java/export/database.json";

        // Structure de données pour stocker les données formatées
        Map<String, List<List<String>>> formattedData = new HashMap<>();

        // Parcourir chaque ligne de données
        for (String line : data) {
            String[] parts = line.split(",");
            if (parts.length < 2) {
                System.err.println("Attention : La ligne de données est mal formatée : " + line);
                continue; // Ignorer la ligne mal formatée
            }

            String tableName = parts[0];
            List<List<String>> tableData = formattedData.computeIfAbsent(tableName, k -> new ArrayList<>());
            List<String> rowData = new ArrayList<>();

            // Ajouter les valeurs de la ligne à la liste de données de la table
            for (int i = 1; i < parts.length; i++) {
                rowData.add(parts[i]);
            }

            // Ajouter la liste de données à la liste des données de la table
            tableData.add(rowData);
        }

        // Convertir la structure de données en format JSON et écrire dans le fichier
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile(); // Créer le fichier s'il n'existe pas
            } try (FileWriter fileWriter = new FileWriter(filePath)) {
                objectMapper.writeValue(fileWriter, formattedData);
                System.out.println("Export JSON réussi pour toutes les tables de la base de données !");
            }
        }catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'export JSON : " + e.getMessage(), e);
        }

    }


    public static void main(String[] args) {
        Producer2Consumer3 Pr2Cs3 = new Producer2Consumer3();
        Pr2Cs3.start();
    }
}

