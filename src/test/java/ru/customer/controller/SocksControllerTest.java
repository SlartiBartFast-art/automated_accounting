package ru.customer.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import ru.customer.model.ColorDto;
import ru.customer.model.Sock;
import ru.customer.model.SockDto;

import java.util.List;
import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SocksControllerTest {

    private static final String SOCK = "http://localhost:8080/sock/";
    private static final String SOCK_ALL = "http://localhost:8080/sock/socksAll";

    @Autowired
    private TestRestTemplate testRestTemplate;

    private SockDto sockDto1;
    private Long rsl;
    private ResponseEntity<Sock> sock;

    @BeforeEach
    void setUp() {
        ColorDto color = new ColorDto(0L, "Red");
        SockDto sockDto = new SockDto(0, 75, 10, color);
        ColorDto color1 = new ColorDto(0L, "Black");
        sockDto1 = new SockDto(0, 55, 15, color1);
        testRestTemplate.postForEntity(SOCK, sockDto, Sock.class);
        sock = testRestTemplate.postForEntity(SOCK, sockDto1, Sock.class);
        rsl = Objects.requireNonNull(sock.getBody()).getId();
        /*var socks = testRestTemplate.exchange(
                SOCK_ALL,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Sock>>() {
                }
        ).getBody();
        System.out.println("RETURN -> " + socks);*/
    }

    @Sql(scripts = "/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @AfterEach
    void tearDown() {
    }

    @Test
    void save() {
        Assertions.assertEquals(HttpStatus.CREATED, sock.getStatusCode());
    }

    @Test
    void delete() {
        testRestTemplate.delete(SOCK + rsl);
        ResponseEntity<Sock> response3 = testRestTemplate.
                getForEntity(SOCK + rsl,
                        Sock.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response3.getStatusCode());
    }
}