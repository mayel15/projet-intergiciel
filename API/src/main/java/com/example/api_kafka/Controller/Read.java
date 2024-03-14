package com.example.api_kafka.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

@RestController
@RequestMapping("/read")
public class Read {

    @PostMapping("hl7")
    public ResponseEntity<String> readHl7(@RequestBody String xmlMessage) {
        try {
            // Créer un constructeur de documents
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Parser le XML
            Document document = builder.parse(new InputSource(new StringReader(xmlMessage)));

            // Récupérer les éléments pertinents du XML et les mettre dans un objet JSON
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode json = objectMapper.createObjectNode();

            // Exemple de conversion pour le nom, prénom et IPP
            NodeList pidList = document.getElementsByTagName("PID");
            for (int i = 0; i < pidList.getLength(); i++) {
                Node pidNode = pidList.item(i);
                if (pidNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element pidElement = (Element) pidNode;
                    String ipp = pidElement.getElementsByTagName("PID.3.1").item(0).getTextContent(); // Récupérer l'IPP
                    String nom = pidElement.getElementsByTagName("PID.5.1").item(0).getTextContent(); // Récupérer le nom
                    String prenom = pidElement.getElementsByTagName("PID.5.2").item(0).getTextContent(); // Récupérer le prénom
                    json.put("ipp", ipp); // Stocker l'IPP dans le JSON
                    json.put("nom", nom); // Stocker le nom dans le JSON
                    json.put("prenom", prenom); // Stocker le prénom dans le JSON
                    // Sortir de la boucle après la première occurrence de PID
                    break;
                }
            }

            // Convertir l'objet JSON en String
            String jsonResponse = objectMapper.writeValueAsString(json);

            // Imprimer le JSON pour le débogage
            System.out.println("\nJSON extrait : ");
            System.out.println(jsonResponse);

            // Vous pouvez manipuler ou renvoyer jsonResponse selon vos besoins
            return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
        } catch (Exception e) {
            // Gérer les erreurs de conversion
            e.printStackTrace();
            return new ResponseEntity<>("Erreur lors de l'extraction des informations du XML", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
