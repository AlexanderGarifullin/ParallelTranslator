package org.tba.paralleltranslator.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tba.paralleltranslator.interfaces.ApiTranslationRequestsLogsDAO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 * Implementation of {@link ApiTranslationRequestsLogsDAO} that uses JDBC to interact with the database.
 * This class handles the persistence of translation request logs into the database.
 */
@Component
public class ApiTranslationRequestsLogsDAOImpl implements ApiTranslationRequestsLogsDAO {

    private final DataSource dataSource;

    /**
     * Constructs a new ApiTranslationRequestsLogsDAOImpl with the given DataSource.
     *
     * @param dataSource the DataSource used to obtain database connections
     */
    @Autowired
    public ApiTranslationRequestsLogsDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Saves the details of a translation request into the database.
     *
     * @param clientIp the IP address of the client making the request
     * @param text the original text to be translated
     * @param translatedText the translated text
     */
    @Override
    public void save(String clientIp, String text, String translatedText) {
        String sql = "INSERT INTO api_translation_requests_logs (client_ip, text, translated_text) VALUES (?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, clientIp);
            preparedStatement.setString(2, text);
            preparedStatement.setString(3, translatedText);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
