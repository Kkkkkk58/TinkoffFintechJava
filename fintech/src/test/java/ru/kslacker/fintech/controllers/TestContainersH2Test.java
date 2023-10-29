package ru.kslacker.fintech.controllers;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.core.H2Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.sql.Connection;
import java.sql.DriverManager;

@SpringBootTest
public abstract class TestContainersH2Test {

    @Container
    public static GenericContainer<?> h2 = new GenericContainer(DockerImageName.parse("oscarfonts/h2"))
            .withExposedPorts(1521, 81)
            .withEnv("H2_OPTIONS", "-ifNotExists")
            .waitingFor(Wait.defaultWaitStrategy());

    @DynamicPropertySource
    static void dataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.driverClassName", () -> "org.h2.Driver");
        registry.add("spring.datasource.url", TestContainersH2Test::getDataSourceUrl);
        registry.add("spring.datasource.username", TestContainersH2Test::getDataSourceUsername);
    }

    private Connection connection;
    private Liquibase liquibase;

    @BeforeEach
    void setup() throws Exception {
        String jdbcUrl = getDataSourceUrl();
        connection = DriverManager.getConnection(jdbcUrl, getDataSourceUsername(), getDataSourcePassword());
        Database database = new H2Database();
        database.setConnection(new JdbcConnection(connection));
        liquibase = new Liquibase("db/changelog/db.changelog-master.yaml", new ClassLoaderResourceAccessor(), database);
        liquibase.update("");
    }

    @AfterEach
    void cleanup() throws Exception {
        liquibase.dropAll();
        connection.close();
    }

    @Test
    public void contextLoads() {
    }

    private static String getDataSourceUrl() {
        return "jdbc:h2:tcp://localhost:" + h2.getMappedPort(1521) + "/test";
    }

    private static String getDataSourceUsername() {
        return "sa";
    }

    private static String getDataSourcePassword() {
        return "";
    }
}
