package org.example;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;


public class DatabaseConnectionTest {

    private static final String URL = "jdbc:mysql://localhost::3306/car_rental";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    @Test
    void testMySQLConnection(){
        try{
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            assertThat(connection).isNotNull();
            assertThat(connection.isClosed()).isFalse();
            assertThat(connection.isValid(2)).isTrue();

            DatabaseMetaData metaData = connection.getMetaData();
            System.out.println("Anslutning Lyckades!");
            System.out.println("Databasklient:"+metaData.getDatabaseProductName());
            System.out.println("Version:"+metaData.getDatabaseProductVersion());

            connection.close();




        } catch (Exception e) {
            System.err.println("Anslutning misslyckades!"+ e.getMessage());
            throw new AssertionError("kunde inte ansluta till server kolla så du är upkopllad");
        }
    }

}
