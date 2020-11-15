package com.example.demo.util;

import com.example.demo.DemoApplication;
import com.example.demo.service.TransactionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class ReadXmlTest {

    @Autowired
    private ReadXml readXml;
    @Autowired
    private TransactionService transactionService;

    /**
     * Test if the method ReadXml.readFile() reads, parses and saves data from file in database.
     * Asserts show that transactions were created, saved and it's quantity equals 12 - the quantity in file.
     */
    @Test
    public void readFileTest() {
        readXml.readFile();
        assertFalse(transactionService.findAll().isEmpty());
        assertEquals(12, transactionService.findAll().size());
    }
}
