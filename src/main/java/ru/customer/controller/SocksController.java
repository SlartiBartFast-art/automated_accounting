package ru.customer.controller;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import ru.customer.model.Color;
import ru.customer.model.Sock;
import ru.customer.model.SockDto;
import ru.customer.model.SockResponse;
import ru.customer.service.SockServiceImpl;
import ru.customer.utils.AppConstants;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.StreamSupport;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@Validated
@RestController
@RequestMapping("/sock")
@AllArgsConstructor
public class SocksController {

    static final Logger LOGGER = LoggerFactory.getLogger(SocksController.class);

    private final SockServiceImpl service;

    private final ModelMapper modelMapper; //to do

    /**
     * findALL Sock in DB
     * Возвращает общее количество носков на складе,
     * соответствующих переданным в параметрах критериям запроса.
     *
     * @return List<Sock> Entity
     */
    @GetMapping("/")
    public List<Sock> findAll() {
        return StreamSupport.stream(
                this.service.findAll().spliterator(), false
        ).toList();
    }

    /**
     * Pagination and Sorting Example
     *
     * @param pageNo   page number
     * @param pageSize number of entities per page
     * @param sortBy   sort in ascending or descending order
     * @param sortDir  default as ascending
     * @return CarResponse entity
     */
    @GetMapping("/socks")
    public SockResponse getAllPosts(
            @RequestParam(value = "pageNo",
                    defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize",
                    defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy",
                    defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir",
                    defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return service.getAllSock(pageNo, pageSize, sortBy, sortDir);
    }

    /**
     * //todo validation
     * findALL Sock in DB, corresponding to the request criteria
     * Возвращает общее количество носков на складе,
     * соответствующих переданным в параметрах критериям запроса.
     *
     * @return
     */
    @GetMapping("/socks/")
    public ResponseEntity<List<Sock>> findAllLike(@RequestParam String color,
                                                  @RequestParam String operator,
                                                  @RequestParam String cottonPart
    ) {
        LOGGER.info("Что пришло ->{}", color + operator + cottonPart);
        if (!color.equals(service.matchesColor(color))
                || !operator.equals(service.matchesOperator(operator))
                || !NumberUtils.isCreatable(cottonPart)
                || cottonPart == null
        ) {
            return new ResponseEntity<>(
                    List.of(Sock.of(new Color(), 0, 0)),
                    HttpStatus.BAD_REQUEST
            );
        }
        if (color == null
                || operator == null
                || Integer.parseInt(cottonPart) <= 0
        ) {
            return new ResponseEntity<>(
                    List.of(Sock.of(new Color(0L, color), Integer.parseInt(cottonPart), 0)),
                    HttpStatus.BAD_REQUEST
            );
        }
        var rsl = service.findByOperator(
                color,
                operator,
                Integer.parseInt(cottonPart));
        LOGGER.info("Что нашлось -> {}", rsl);
        return new ResponseEntity<>(rsl,
                rsl.get(0).getColor() != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    /**
     * Регистрирует приход носков на склад.
     * Параметры запроса передаются в теле запроса в виде JSON-объекта со следующими
     * атрибутами:
     * color — цвет носков, строка (например, black, red, yellow);
     * cottonPart — процентное содержание хлопка в составе носков,
     * целое число от 1 до 100 (например, 30, 18, 42);
     * quantity — количество пар носков, целое число больше 0.
     * Результаты:
     * HTTP 201 — удалось добавить приход;
     * HTTP 400 — параметры запроса отсутствуют или имеют некорректный формат;
     * HTTP 500 — произошла ошибка, не зависящая от вызывающей стороны (например, база данных недоступна).
     *
     * @param sock SockDto Object
     * @return ResponseEntity<Sock>
     */
    @PostMapping("/")
    public ResponseEntity<Sock> save(@Valid @RequestBody SockDto sock) {
        Sock result = this.service.save(modelMapper.map(sock, Sock.class));
        if (result.getId() == 0) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "We're sorry, server error, please try again later!"
            );
        }
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    /** //todo service
     * уменьшается
     * Регистрирует отпуск носков со склада. Здесь параметры и результаты аналогичные,
     * но общее количество носков указанного цвета и состава не увеличивается, а уменьшается.
     *
     * @param sock SockDto Object
     * @return ResponseEntity<Sock>
     */
    @PostMapping("/outcome")
    public ResponseEntity<Sock> outcome(@Valid @RequestBody SockDto sock) {
        Sock rsl = this.service.reduceSockQuantity(modelMapper.map(sock, Sock.class));
        if (rsl.getId() == 0) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "We're sorry, server error, please try again later!"
            );
        }
        return new ResponseEntity<>(rsl, HttpStatus.OK);
    }

    /**
     * The remove Sock object by Id
     *
     * @param id Sock object
     * @return HTTP status code
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") @Min(1) Long id) {
        if (id > service.findIdLastEntity()) {
            throw new IllegalArgumentException(
                    "The object id must be correct, object like this id don't exist!");
        }
        return new ResponseEntity<>(service.deleteById(id) ? OK : NOT_FOUND);
    }
}
