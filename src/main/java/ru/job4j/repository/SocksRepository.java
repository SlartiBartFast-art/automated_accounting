package ru.job4j.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.job4j.model.Sock;

import java.util.List;

/**
 * REpo Sock model
 */
public interface SocksRepository extends CrudRepository<Sock, Integer> {

    @Query("select u from Sock as u where u.color = ?1")
    public List<Sock> findAllByColor(String color);

    @Query("select u from Sock as u where u.cottonPart = ?1")
    public List<Sock> findAllByCottonPart(int cottonPart);

    /**
     * найти сущность
     * @param color
     * @param cottonPart
     * @return
     */
    @Query("select u from Sock as u where u.color = ?1 and u.cottonPart = ?2")
    public Sock findByColorAndCottonPartWithin(String color, int cottonPart);

    @Query("select u from Sock as u where u.color = ?1 and u.cottonPart >= ?2")
    public List<Sock> findAllByColorAndCottonPartAndMore(String color, int symbol);

    @Query("select u from Sock as u where u.color = ?1 and u.cottonPart <= ?2")
    public List<Sock> findAllByColorAndCottonPartAndSmaller(String color, int symbol);

}
