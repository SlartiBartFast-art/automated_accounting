package ru.job4j.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.job4j.model.Sock;

import java.util.List;
import java.util.Optional;

/**
 * REpo Sock model
 */
public interface SocksRepository extends CrudRepository<Sock, Long> {

    @Query("select u from Sock as u where u.color = ?1")
    List<Sock> findAllByColor(String color);

    @Query("select u from Sock as u where u.cottonPart = ?1")
    List<Sock> findAllByCottonPart(int cottonPart);

    @Query("select u from Sock as u where u.color = ?1 and u.cottonPart = ?2")
    Optional<Sock> findByColorAndCottonPartWithin(String color, int cottonPart);

    @Query("select u from Sock as u where u.color = ?1 and u.cottonPart >= ?2")
    List<Sock> findAllByColorAndCottonPartAndMore(String color, int symbol);

    @Query("select u from Sock as u where u.color = ?1 and u.cottonPart <= ?2")
    List<Sock> findAllByColorAndCottonPartAndSmaller(String color, int symbol);

    Optional<Sock> findFirstByOrderByIdDesc();

}
