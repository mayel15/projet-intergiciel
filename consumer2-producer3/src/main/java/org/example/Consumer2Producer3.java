package org.example;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.example.models.MovementModel;
import org.example.models.PatientModel;
import org.example.models.StayModel;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.*;


public class Consumer2Producer3 {

    private static final String TOPIC_2 = "topic2";
    private static final String TOPIC_3 = "topic3";
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String GROUP_ID = "group2";

    private Producer<String, ArrayList<String>> producer;
    private Consumer<String, String> consumer;
    private Connection connection;

    public Consumer2Producer3() {
        initializeProducer();
        initializeConsumer();
        initializeDatabase();
    }

    private void initializeProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ArrayListSerializer.class.getName());
        producer = new org.apache.kafka.clients.producer.KafkaProducer<>(props);
    }

    private void initializeConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(TOPIC_2));
    }

    private void initializeDatabase() {
        String url = "jdbc:postgresql://localhost:5435/mirthdb";
        String user = "mirthdb";
        String password = "mirthdb";

        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void start() throws SQLException{
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(100);
                records.forEach(record -> {
                    String key = record.key();
                       String value = record.value();
                    // Fetch data from database based on the received value
                    try {
                        ArrayList<String> dataResult = getDataQuery(key, value);
                        producer.send(new ProducerRecord<>(TOPIC_3, key, dataResult));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } finally {
            producer.close();
            consumer.close();
        }
    }

    // convertit les commandes envoyé depuis la console en SQL ou pas
    private ArrayList<String> getDataQuery(String key, String value) throws SQLException {
        ArrayList<String> data = null;
        switch (key) {
            case "get_all_patients":
                data = getAllPatients();
                break;
            case "get_patient_by_pid":
                data = getPatientByPid(value);
                break;
            case "get_patient_by_name":
                data = getPatientByName(value);
                break;
            case "get_patient_stay_by_pid":
                data =getPatientStayByPid(value);
                break;
            case "get_patient_movements_by_sid":
                data = getPatientMovementsBySid(value);
                break;
            case "export":
                data = exportDatabaseToJson();
                break;
            case "help":
                data = getHelp();
                break;
            case "exit":
                data = new ArrayList<>();
                data.add("Au revoir!");
                break;
            default:
                System.out.println("Commande inconnue");
                data = new ArrayList<>();
                data.add("Commande non reconnue. Tapez 'help' pour afficher la liste des commandes disponibles.");
        }
        return data;
    }

    // recupere tous les données des patients
    private ArrayList<String> getAllPatients() throws SQLException {
        System.out.println("Execution de la commande 'get_all_patients' ...");
        String query = "SELECT * FROM personsimple";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            ArrayList<String> patientsData = new ArrayList<>();
            while (resultSet.next()) {
                String personId = resultSet.getString("person_id");
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                String naissance = resultSet.getString("naissance");
                String sexe = resultSet.getString("sexe");
                PatientModel patient = new PatientModel(personId, nom, prenom, naissance, sexe);
                patientsData.add(patient.toString());
            }
            if (patientsData.size() == 0) {
                patientsData.add("Pas de patient trouvé");
            }
            //System.out.println(patientsData);
            return patientsData;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // recupere les donnees d'un patient par son pid
    private ArrayList<String> getPatientByPid(String patientId) throws SQLException {
        System.out.println("Execution de la commande: get_patient_by_pid " + patientId);
        String query = "SELECT * FROM personsimple WHERE person_id =" + "'" + patientId + "'";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            ArrayList<String> patientsData = new ArrayList<>();
            while (resultSet.next()) {
                String personId = resultSet.getString("person_id");
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                String naissance = resultSet.getString("naissance");
                String sexe = resultSet.getString("sexe");
                PatientModel patient = new PatientModel(personId, nom, prenom, naissance, sexe);
                patientsData.add(patient.toString());
            }
            if (patientsData.size() == 0) {
                patientsData.add("Pas de patient trouvé dont le pid est " + patientId);
            }
            //System.out.println(patientsData);
            return patientsData;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // recupere les données d'un patient par son nom
    private ArrayList<String> getPatientByName(String name) {
        System.out.println("Execution de la commande: get_patient_by_name " + name);
        String query = "SELECT * FROM personsimple WHERE nom =" + "'" + name + "'";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            ArrayList<String> patientsData = new ArrayList<>();
            while (resultSet.next()) {
                String personId = resultSet.getString("person_id");
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                String naissance = resultSet.getString("naissance");
                String sexe = resultSet.getString("sexe");
                PatientModel patient = new PatientModel(personId, nom, prenom, naissance, sexe);
                patientsData.add(patient.toString());
            }
            if (patientsData.size() == 0) {
                patientsData.add("Pas de patient trouvé dont le nom est " + name);
            }
            //System.out.println(patientsData);
            return patientsData;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<String> getPatientStayByPid(String patientId) {
        System.out.println("Execution de la commande: get_patient_stay_by_pid " + patientId);
        String query = "SELECT * FROM stay WHERE person_id =" + "'" + patientId + "'";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            ArrayList<String> stayData = new ArrayList<>();
            while (resultSet.next()) {
                String personId = resultSet.getString("person_id");
                String numeroSejour = resultSet.getString("numero_sejour");
                String dateDebut = resultSet.getString("date_debut");
                String dateFin = resultSet.getString("date_fin");
                StayModel stay = new StayModel(personId, numeroSejour, dateDebut, dateFin);
                stayData.add(stay.toString());
            }
            if (stayData.size() == 0) {
                stayData.add("Pas de sejour trouvé pour le patient dont le pid est " + patientId);
            }
            //System.out.println(stayData);
            return stayData;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<String> getPatientMovementsBySid(String stayId) {
        System.out.println("Execution la commande: get_patient_movements_by_sid " + stayId);
        String query = "SELECT * FROM movements WHERE numero_sejour = " + "'" + stayId + "'";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            ArrayList<String> movementsData = new ArrayList<>();
            while (resultSet.next()) {
                String personId = resultSet.getString("person_id");
                String numeroSejour = resultSet.getString("numero_sejour");
                String service = resultSet.getString("service");
                String date_mouvement = resultSet.getString("date_mouvement");
                MovementModel movement = new MovementModel(personId, numeroSejour, service, date_mouvement);
                movementsData.add(movement.toString());
            }
            if (movementsData.size() == 0) {
                movementsData.add("Pas de mouvement trouvé pour le patient dont le numero de sejour est " + stayId);
            }
            //System.out.println(movementsData);
            return movementsData;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<String> getHelp() {
        System.out.print("Execution de la commande help ...");
        ArrayList<String> helpCommands = new ArrayList<>();
        helpCommands.add("Liste des commandes disponibles :");
        helpCommands.add("- get_all_patients : retourne tous les patients");
        helpCommands.add("- get_patient_by_pid : retourne l'identité complète d'un patient par son identifiant PID-3");
        helpCommands.add("- get_patient_by_name : retourne l'identité d'un patient par son nom");
        helpCommands.add("- get_patient_stay_by_pid : retourne les séjours d'un patient par son identifiant PID-3");
        helpCommands.add("- get_patient_movements_by_sid : retourne tous les mouvements d'un patient par le numéro de séjour");
        helpCommands.add("- export : exporte les données de la base de données en JSON dans un fichier");
        helpCommands.add("- help : affiche la liste des commandes et une explication comme ci-dessus");
        helpCommands.add("- exit : quitte la console");
        return helpCommands;
    }

    // met les donnees dans une array list de string
    private ArrayList<String> exportDatabaseToJson() {
        System.out.println("Export de la base de données en json ...");
        ArrayList<String> allTablesData = new ArrayList<>();

        try {
            // Récupérer les données de chaque table
            fetchDataFromTable( "PersonSimple", allTablesData);
            fetchDataFromTable("Address", allTablesData);
            fetchDataFromTable( "Movements", allTablesData);
            fetchDataFromTable( "Stay", allTablesData);

            return allTablesData;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des données : " + e.getMessage(), e);
        }
    }

    // recperer les données depuis les tables
    private void fetchDataFromTable(String tableName, ArrayList<String> allTablesData) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);

        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (rs.next()) {
            StringBuilder rowData = new StringBuilder();
            for (int i = 1; i <= columnCount; i++) {
                String columnValue = rs.getString(i);
                rowData.append(columnValue).append(",");
            }
            // Supprimer la virgule finale
            rowData.deleteCharAt(rowData.length() - 1);
            allTablesData.add(tableName + "," + rowData.toString());
        }

        rs.close();
        stmt.close();
    }

    public static void main(String[] args) throws SQLException {
        Consumer2Producer3 Cs2Pr3 = new Consumer2Producer3();
        Cs2Pr3.start();
    }
}

