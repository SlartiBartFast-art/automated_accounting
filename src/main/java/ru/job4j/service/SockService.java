package ru.job4j.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.job4j.model.Sock;
import ru.job4j.repository.SocksRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SockService {

    static final Logger LOGGER = LoggerFactory.getLogger(SockService.class);

    private final SocksRepository repository;

    public SockService(SocksRepository socksRepository) {
        this.repository = socksRepository;
    }

    public Iterable<Sock> findAll() {
        return repository.findAll();
    }

    public Optional<Sock> findById(int id) {
        LOGGER.info("ID->: {}", repository.findById(id).get());
        return repository.findById(id);
    }

    /**
     * color — цвет носков, строка (например, black, red, yellow);
     *
     * @param color
     * @return
     */
    public List<Sock> findByColor(String color) {
        return repository.findAllByColor(color);
    }

    /**
     * cottonPart — процентное содержание хлопка в составе носков,
     * целое число от 0 до 100 (например, 30, 18, 42);
     *
     * @param cottonPart
     * @return
     */
    public List<Sock> findByCottonPart(int cottonPart) {
        return repository.findAllByCottonPart(cottonPart);
    }

    /**
     * получить все пару носков где color and cottonPart совпадают заданных параметрам
     *
     * @param color
     * @param cottonPath
     * @return
     */
    public List<Sock> findByColorAndCottonPart(String color, int cottonPath) {
        return repository.findAllByColorAndCottonPart(color, cottonPath);
    }

    /**
     * вернуть результат выборки по параметрам запроса UI
     * @param color цвет
     * @param operator оператор выборки
     * @param cottonPart процентное соотношение хлопка
     * @return
     */
    public List<Sock> findByOperator(String color, String operator, int cottonPart) {
        if (operator.equals("moreThan")) {
            return repository.findAllByColorAndCottonPartAndMore(color, cottonPart);
        }
        if (operator.equals("lessThan")) {
            return repository.findAllByColorAndCottonPartAndSmaller(color, cottonPart);
        }
        return repository.findAllByColorAndCottonPart(color, cottonPart);
    }

}
