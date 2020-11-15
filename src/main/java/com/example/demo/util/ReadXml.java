package com.example.demo.util;

import com.example.demo.model.Client;
import com.example.demo.model.Transaction;
import com.example.demo.model.enums.Currency;
import com.example.demo.service.ClientService;
import com.example.demo.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * Class works xml file
 */
@Component
@Slf4j
public class ReadXml {

    @Value(value = "src\\main\\resources\\data\\Java_test.xml")
    private String fileDestination; //xml file with data
    @Autowired
    private ClientService clientService;
    @Autowired
    private TransactionService transactionService;

    /**
     * Method reads xml file, parses it and saves data in database
     */
    public void readFile() {
        try {
            File fXmlFile = new File(fileDestination);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            //Get elements with tag "transaction"
            NodeList transactionNodes = doc.getElementsByTagName("transaction");
            if (transactionNodes.getLength() > 0) {
                for (int i = 0; i < transactionNodes.getLength(); i++) {
                    Node trNode = transactionNodes.item(i);
                    Element eElement = (Element) trNode;
                    Client client = null;
                    //Get inn from data, it is the unique value for client and client can be identified by inn
                    Integer inn = Integer.parseInt(eElement.getElementsByTagName("inn").item(0)
                            .getTextContent());
                    //Check if the client with this inn exists in database, if exist, take this existed client
                    Client existedClient =  clientService.findByInn(inn);
                    if (existedClient != null) {
                        client = existedClient;
                    } else {
                        client = new Client();
                        client.setInn(inn);
                        client.setFirstName((eElement.getElementsByTagName("firstName").item(0)
                                .getTextContent()));
                        client.setLastName((eElement.getElementsByTagName("lastName").item(0)
                                .getTextContent()));
                        client.setMiddleName((eElement.getElementsByTagName("middleName").item(0)
                                .getTextContent()));
                        client = clientService.save(client);
                    }
                    //Create and save transaction
                    if (trNode.getNodeType() == Node.ELEMENT_NODE) {
                        Transaction transaction = new Transaction();
                        transaction.setPlace(eElement.getElementsByTagName("place").item(0)
                                .getTextContent());
                        transaction.setAmount(Double.parseDouble(eElement.getElementsByTagName("amount").item(0)
                                .getTextContent()));
                        transaction.setCurrency(Currency.valueOf(eElement.getElementsByTagName("currency").item(0)
                                .getTextContent()));
                        transaction.setCard(eElement.getElementsByTagName("card").item(0)
                                .getTextContent());
                        transaction.setClient(client);
                        transactionService.save(transaction);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Can't parse file!");
        }
    }

}
