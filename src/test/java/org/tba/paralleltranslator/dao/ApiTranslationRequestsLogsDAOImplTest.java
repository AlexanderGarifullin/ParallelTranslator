package org.tba.paralleltranslator.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.tba.paralleltranslator.interfaces.ApiTranslationRequestsLogsDAO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@ComponentScan("org.tba.paralleltranslator.dao")
@TestPropertySource(locations = "classpath:application-test.properties")
class ApiTranslationRequestsLogsDAOImplTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ApiTranslationRequestsLogsDAO apiTranslationRequestsLogsDAO;

    @Test
    public void testSave() throws Exception {
        String clientIp = "127.0.0.1";
        String text = "Hello";
        String translatedText = "Привет";

        apiTranslationRequestsLogsDAO.save(clientIp, text, translatedText);

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM api_translation_requests_logs WHERE client_ip = '127.0.0.1'")) {

            assertTrue(resultSet.next());
            assertEquals(clientIp, resultSet.getString("client_ip"));
            assertEquals(text, resultSet.getString("text"));
            assertEquals(translatedText, resultSet.getString("translated_text"));
        }
    }
}
