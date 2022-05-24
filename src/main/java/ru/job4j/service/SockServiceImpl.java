package ru.job4j.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.job4j.model.Sock;
import ru.job4j.model.SockResponse;
import ru.job4j.repository.SocksRepository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Data
@AllArgsConstructor
@Service
public class SockServiceImpl implements SockService {

    static final Logger LOGGER = LoggerFactory.getLogger(SockServiceImpl.class);

    private final ConcurrentHashMap<String, String> colors = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> operators = new ConcurrentHashMap<>();

    private final SocksRepository repository;

    @PostConstruct
    private void init() {
        initialization();
    }

    void initialization() {
        colors.put("white", "white");
        colors.put("black", "black");
        colors.put("red", "red");
        colors.put("blue", "blue");
        colors.put("green", "green");
        colors.put("yellow", "yellow");
        colors.put("orange", "orange");
        colors.put("gray", "gray");
        operators.put("moreThan", "moreThan");
        operators.put("lessThan", "lessThan");
        operators.put("equal", "equal");
    }

    public String matchesColor(String string) {
        String rsl = "not registered";
        if (colors.contains(string)) {
            return colors.get(string);
        }
        return rsl;
    }

    public String matchesOperator(String operator) {
        String rsl = "not registered";
        if (operators.contains(operator)) {
            return operators.get(operator);
        }
        return rsl;
    }

    public Iterable<Sock> findAll() {
        return repository.findAll();
    }

    /** //todo
     * Найти по параметру цвет и % хлопка и увеличить на кол-во единиц Quantity,
     * Сохранить сущность в БД согласно указанных параметров
     *
     * @param sock Модель Sock
     * @return сохраненная сущность Sock c увеличенным числом пар
     */
    public Sock save(Sock sock) {
        var rsl = repository.findByColorAndCottonPartWithin(sock.getColor(), sock.getCottonPart());
        if (rsl.isPresent()) {
            var tempSock = rsl.get();
            var count = tempSock.getQuantity() + sock.getQuantity();
            tempSock.setQuantity(count);
            return repository.save(tempSock);
        }
        return repository.save(sock);
    }

//todo color
    /**
     * Найти по параметрам color and cottonPart
     * и уменьшить количество единиц товара(Quantity) согласно заданного параметра
     *
     * @param sock сущность
     * @return Sock entity from DB
     */
    public Sock reduceSockQuantity(Sock sock) {
        var rsl = repository
                .findByColorAndCottonPartWithin(sock.getColor().getColoring(),
                sock.getCottonPart());
        if (rsl.isPresent()) {
            var tempSock = rsl.get();
            var count = tempSock.getQuantity() - sock.getQuantity();
            tempSock.setQuantity(count);
            if (tempSock.getQuantity() > 0) {
                return repository.save(tempSock);
            }
            repository.delete(tempSock);
        }
        return Sock.of(null, 0, 0);
    }

    /**
     * найти по ID
     *
     * @param id int
     * @return Optional<Sock>
     */
    public Optional<Sock> findById(int id) {
        return repository.findById(id);
    }

    /**
     * найти согласно цвета сущности
     * color — цвет носков, строка (например, black, red, yellow);
     *
     * @param color
     * @return List<Sock>
     */
    public List<Sock> findByColor(String color) {
        return repository.findAllByColor(color);
    }

    /**
     * cottonPart — процентное содержание хлопка в составе носков,
     * целое число от 0 до 100 (например, 30, 18, 42);
     *
     * @param cottonPart
     * @return List<Sock>
     */
    public List<Sock> findByCottonPart(int cottonPart) {
        return repository.findAllByCottonPart(cottonPart);
    }

    /**
     * получить все пару носков где color and cottonPart совпадают заданных параметрам
     *
     * @param color
     * @param cottonPath
     * @return Sock Entity
     */
    public Sock findByColorAndCottonPart(String color, int cottonPath) {
        var rsl = repository.findByColorAndCottonPartWithin(color, cottonPath);
        if (rsl.isPresent()) {
            return rsl.get();
        }
        return Sock.of(null, 0, 0);
    }

    /**
     * найти по 2-м параметрам и уменьшить общее колл-во на 3-й параметр
     *
     * @param sock запрос с объектом
     * @return sock итоговый остаток объектов в БД
     */
    public Sock reducesByQuantity(Sock sock) {
        var rsl = findByColorAndCottonPart(sock.getColor(), sock.getCottonPart());
        LOGGER.info("what find {}", rsl);
        var count = rsl.getQuantity() - sock.getQuantity();
        rsl.setQuantity(count);
        repository.save(rsl);
        return rsl;
    }

    /**
     * Возвращает общее количество носков на складе,
     * соответствующих переданным в параметрах критериям запроса.
     * вернуть результат выборки по параметрам запроса UI
     * operation —  moreThan, больше чем, в данном случае строго ограничен  снизу  %  хлопка + цвет
     * operation — lessThan, меньше чем, в данном случае строго ограничен  сверху  %  хлопка + цвет
     * operation — equal, в данном случае строго ограничен цвет и %  хлопка
     *
     * @param color      цвет
     * @param operator   оператор выборки
     * @param cottonPart процентное соотношение хлопка
     * @return List<Sock> Entity
     */
    public List<Sock> findByOperator(String color, String operator, int cottonPart) {
        if (operator.equals("moreThan")) {
            return repository.findAllByColorAndCottonPartAndMore(color, cottonPart);
        }
        if (operator.equals("lessThan")) {
            return repository.findAllByColorAndCottonPartAndSmaller(color, cottonPart);
        }
        return List.of(findByColorAndCottonPart(color, cottonPart));
    }

    /**
     * Pagination and Sorting Example
     * @param pageNo page number
     * @param pageSize number of entities per page
     * @param sortBy sort in ascending or descending order
     * @param sortDir default as ascending
     * @return CarResponse entity
     */
    @Override
    public SockResponse getAllSock(int pageNo, int pageSize, String sortBy, String sortDir) {
        return null;
    }
}
