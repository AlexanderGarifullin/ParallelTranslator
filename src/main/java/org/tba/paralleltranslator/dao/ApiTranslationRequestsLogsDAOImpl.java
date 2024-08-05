package org.tba.paralleltranslator.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tba.paralleltranslator.interfaces.ApiTranslationRequestsLogsDAO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class ApiTranslationRequestsLogsDAOImpl implements ApiTranslationRequestsLogsDAO {

    private final DataSource dataSource;

    @Autowired
    public ApiTranslationRequestsLogsDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

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
