package ru.customer.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.customer.model.Color;
import ru.customer.model.Sock;
import ru.customer.model.SockResponse;
import ru.customer.repository.ColorRepositoryImpl;
import ru.customer.repository.SocksRepository;
import ru.customer.utils.AppConstants;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Data
@AllArgsConstructor
@Service
public class SockServiceImpl implements SockService {

    static final Logger LOGGER = LoggerFactory.getLogger(SockServiceImpl.class);

    private final ConcurrentHashMap<String, String> operators = new ConcurrentHashMap<>();

    private final SocksRepository repository;
    private final ColorRepositoryImpl colorRepository;

    @PostConstruct
    private void init() {
        initialization();
    }

    void initialization() {
        operators.put("moreThan", "moreThan");
        operators.put("lessThan", "lessThan");
        operators.put("equal", "equal");
    }

    public String matchesColor(String coloring) {
        return colorRepository.existsColorByColoring(coloring)
                ? colorRepository.findColorByColoring(coloring).get().getColoring()
                : AppConstants.RESPONSE;
    }

    public String matchesOperator(String operator) {
        return operators.contains(operator)
                ? operators.get(operator)
                : AppConstants.RESPONSE;
    }

    /**
     * Найти по параметру цвет и % хлопка и увеличить на кол-во единиц Quantity,
     * Сохранить сущность в БД согласно указанных параметров
     *
     * @param sock Модель Sock
     * @return сохраненная сущность Sock c увеличенным числом пар
     */
    public Sock save(Sock sock) {
        var color = findByColor(sock.getColor().getColoring());
        if (color.isPresent()) {
            var rsl = repository
                    .findSockByColorAndCottonPart(color.get(), sock.getCottonPart());
            LOGGER.info("Что нашли -> {}", rsl);
            if (rsl.isPresent()) {
                var tempSock = rsl.get();
                tempSock.setQuantity(tempSock.getQuantity() + sock.getQuantity());
                return repository.save(tempSock);
            }
        }
        sock.setColor(colorRepository.save(sock.getColor()));
        return repository.save(sock);
    }

    private Optional<Color> findByColor(String coloring) {
        return colorRepository.findColorByColoring(coloring);
    }

    public boolean parameterMatching(String coloring, String operator, String cottonPart) {
        return !coloring.equals(matchesColor(coloring))
                || !operator.equals(matchesOperator(operator))
                || !NumberUtils.isCreatable(cottonPart)
                || Integer.parseInt(cottonPart) <= 0;
    }

    /**
     * Найти по параметрам color and cottonPart
     * и уменьшить количество единиц товара(Quantity) согласно заданного параметра
     *
     * @param sock сущность
     * @return Sock entity from DB
     */
    public Sock reduceSockQuantity(Sock sock) {
        var rsl = repository
                .findSockByColorAndCottonPart(sock.getColor(),
                        sock.getCottonPart());
        return findAndDeleteSock(rsl, sock);
    }

    private Sock findAndDeleteSock(Optional<Sock> rsl, Sock sock) {
        if (rsl.isPresent()) {
            var tempSock = rsl.get();
            if (tempSock.getQuantity() > sock.getQuantity()) {
                tempSock.setQuantity(tempSock.getQuantity() - sock.getQuantity());
                if (tempSock.getQuantity() > 0) {
                    return repository.save(tempSock);
                }
                repository.delete(tempSock);
            }
        }
        return Sock.of(null, 0, 0);
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
        var rsl = colorRepository.findColorByColoring(color);
        LOGGER.info("Что нашлось в репозитории цвета -> {}", rsl);
        if (rsl.isPresent()) {
            var rslColor = rsl.get();
            if (operator.equals(AppConstants.MORE_THAN)) {
                return repository.findSocksByColorAndCottonPartIsGreaterThanOrderById(rslColor, cottonPart)
                        .orElse(List.of(Sock.of(null, 0, 0)));
            }
            if (operator.equals(AppConstants.LESS_THAN)) {
                return repository.findSocksByColorAndCottonPartAndSmaller(rslColor, cottonPart)
                        .orElse(List.of(Sock.of(null, 0, 0)));
            }

            return List.of(repository.findSockByColorAndCottonPart(rslColor, cottonPart)
                    .orElse(Sock.of(null, 0, 0)));
        }
        return List.of(Sock.of(null, 0, 0));
    }

    /**
     * Find the id of the last saved entity
     *
     * @return Long id entity
     */
    public Long findIdLastEntity() {
        var rsl = repository.findFirstByOrderByIdDesc();
        return rsl.isPresent() ? rsl.get().getId() : 0L;
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
    @Override
    public SockResponse getAllSock(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Sock> socks = repository.findAll(pageable);
        List<Sock> listOfSock = socks.getContent();

        SockResponse sockResponse = new SockResponse();
        sockResponse.setContent(listOfSock);
        sockResponse.setPageNo(socks.getNumber());
        sockResponse.setPageSize(socks.getSize());
        sockResponse.setTotalElements(socks.getTotalElements());
        sockResponse.setTotalPages(socks.getTotalPages());
        sockResponse.setLast(socks.isLast());
        return sockResponse;
    }

    public boolean deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
    /**
     * Find by ID
     *
     * @param id Long
     * @return Optional<Sock>
     */

    public Optional<Sock> findById(Long id) {
        return repository.findById(id);
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

}
