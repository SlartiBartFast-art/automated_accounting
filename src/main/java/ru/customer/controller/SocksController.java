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

import ru.customer.model.*;
import ru.customer.service.SockServiceImpl;
import ru.customer.utils.AppConstants;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.StreamSupport;

import static org.springframework.http.HttpStatus.*;

@Validated
@RestController
@RequestMapping("/sock")
@AllArgsConstructor
public class SocksController {

    static final Logger LOGGER = LoggerFactory.getLogger(SocksController.class);

    private final SockServiceImpl service;

    private final ModelMapper modelMapper;

    /**
     * Pagination and Sorting Example
     *
     * @param pageNo   page number
     * @param pageSize number of entities per page
     * @param sortBy   sort in ascending or descending order
     * @param sortDir  default as ascending
     * @return CarResponse entity
     */
    @GetMapping("/")
    public SockResponse getAllSocks(
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
     * findALL Entity (Sock) in DB, corresponding to the request criteria
     * Возвращает общее количество носков на складе,
     * соответствующих переданным в параметрах критериям запроса.
     *
     * @return ResponseEntity<List < Sock>>
     */
    @GetMapping("/socks")
    public ResponseEntity<List<Sock>> findAllLike(@RequestParam("coloring")
                                                  @NotBlank(message = "Coloring must not be empty!")
                                                          String coloring,
                                                  @NotBlank(message = "Operator must not be empty!")
                                                  @RequestParam("operator")
                                                          String operator,
                                                  @RequestParam("cottonPart")
                                                  @NotBlank(message = "CottonPart must not be empty!")
                                                          String cottonPart) {
        if (service.parameterMatching(coloring, operator, cottonPart)) {
            return new ResponseEntity<>(
                    List.of(Sock.of(new Color(), 0, 0)),
                    BAD_REQUEST
            );
        }
        var rsl = service.findByOperator(
                coloring,
                operator,
                Integer.parseInt(cottonPart));
        return new ResponseEntity<>(rsl,
                rsl.get(0).getColor() != null ? OK : INTERNAL_SERVER_ERROR);
    }

    /**
     * Регистрирует приход Entity на склад.
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
        Sock rsl = this.service.save(modelMapper.map(sock, Sock.class));
        if (rsl.getId() == 0) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "We're sorry, server error, please try again later!"
            );
        }
        return new ResponseEntity<>(rsl, CREATED);
    }

    /**
     * Регистрирует отпуск Entity(носков) со склада.
     * Здесь параметры и результаты аналогичные,
     * но общее количество носков указанного цвета и состава не увеличивается, а уменьшается.
     *
     * @param sock SockDto Object
     * @return ResponseEntity<Sock>
     */
    @PatchMapping("/")
    public ResponseEntity<Sock> updateOutcome(@Valid @RequestBody SockDtoPatch sock) {
        Sock rsl = this.service.reduceSockQuantity(modelMapper.map(sock, Sock.class));
        if (rsl.getId() == 0) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "We're sorry, server error, please try again later!"
            );
        }
        return new ResponseEntity<>(rsl, OK);
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

