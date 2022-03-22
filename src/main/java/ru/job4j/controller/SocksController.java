package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.model.RequestDto;
import ru.job4j.model.Sock;
import ru.job4j.service.SockService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api")
public class SocksController {

    private final SockService service;

    public SocksController(SockService service) {
        this.service = service;
    }

    /**
     * findALL Sock in DB
     * Возвращает общее количество носков на складе,
     * соответствующих переданным в параметрах критериям запроса.
     *
     * @return
     */
    @GetMapping("/socksAll")
    public List<Sock> findAll() {
        return StreamSupport.stream(
                this.service.findAll().spliterator(), false
        ).collect(Collectors.toList());
    }

//TODO как вернуть 3-й 500

    /**
     * findALL Sock in DB
     * Возвращает общее количество носков на складе,
     * соответствующих переданным в параметрах критериям запроса.
     *
     * @return
     */
    @GetMapping("/socks")
    public ResponseEntity<List<Sock>> findAllLike(@ModelAttribute RequestDto requestDto) {
        var c = requestDto.getColor();
        System.out.println("Model Attribute -> " + c);

        var rsl = service.findByOperator(
                requestDto.getColor(),
                requestDto.getOperator(),
                requestDto.getCottonPart());
        if (rsl.equals(null)) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Sock>>(rsl,
                !rsl.isEmpty() ? HttpStatus.OK : HttpStatus.BAD_REQUEST
        );
    }

    /**
     * увеличивается
     * Регистрирует приход носков на склад.
     * Параметры запроса передаются в теле запроса в виде JSON-объекта со следующими
     * атрибутами:
     * color — цвет носков, строка (например, black, red, yellow);
     * cottonPart — процентное содержание хлопка в составе носков,
     * целое число от 0 до 100 (например, 30, 18, 42);
     * quantity — количество пар носков, целое число больше 0.
     * Результаты:
     * HTTP 200 — удалось добавить приход;
     * HTTP 400 — параметры запроса отсутствуют или имеют некорректный формат;
     * HTTP 500 — произошла ошибка, не зависящая от вызывающей стороны (например, база данных недоступна).
     * @param sock
     * @return
     */
    @PostMapping("/income")
    public ResponseEntity<Sock> income(@RequestBody Sock sock) {
        if (sock.getColor().equals(null) || sock.getCottonPart() == 0 || sock.getQuantity() == 0) {
            return new ResponseEntity<>(
                    sock,
                    HttpStatus.BAD_REQUEST
            );
        }
     Sock rsl = this.service.save(sock);
        if (rsl.getId() == 0) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "500 Internal Server Error. Please, check requisites."
            );
        }
        return new ResponseEntity<>(
                rsl,
                HttpStatus.OK
        );
    }

    /**
     * уменьшается
     * Регистрирует отпуск носков со склада. Здесь параметры и результаты аналогичные,
     * но общее количество носков указанного цвета и состава не увеличивается, а уменьшается.
     * @param sock
     * @return
     */
    @PostMapping("/outcome")
    public ResponseEntity<Sock> outcome(@RequestBody Sock sock) {
        if (sock.getColor().equals(null) || sock.getCottonPart() == 0 || sock.getQuantity() == 0) {
            return new ResponseEntity<>(
                    sock,
                    HttpStatus.BAD_REQUEST
            );
        }
        Sock rsl = this.service.reduceSockQuantity(sock);
        if (rsl.getId() == 0) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "500 Internal Server Error. Please, check requisites."
            );
        }
        return new ResponseEntity<>(
                rsl,
                HttpStatus.OK
        );
    }
}

