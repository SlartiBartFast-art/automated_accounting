package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
     * @return
     */
    @GetMapping("/sockAll")
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
     * @return
     */
    @GetMapping("/sock{co}")
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
}
