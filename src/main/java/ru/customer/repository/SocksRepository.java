package ru.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import ru.customer.model.Color;
import ru.customer.model.Sock;

import java.util.List;
import java.util.Optional;

public interface SocksRepository extends JpaRepository<Sock, Long>, JpaSpecificationExecutor<Sock> {

    @Query("select u from Sock as u where u.color = ?1")
    List<Sock> findAllByColor(String color);

    @Query("select u from Sock as u where u.cottonPart = ?1")
    List<Sock> findAllByCottonPart(int cottonPart);

    Optional<Sock> findSockByColorAndCottonPart(Color color, int cottonPart);

    /**
     * @param color
     * @param cottonPart % соотношение хлопка
     * @return
     */
    @Query("select u from Sock as u where u.color = ?1 and u.cottonPart >= ?2")
    Optional<List<Sock>> findSocksByColorAndCottonPartIsGreaterThanOrderById(Color color, int cottonPart);

    @Query("select u from Sock as u where u.color = ?1 and u.cottonPart <= ?2")
    Optional<List<Sock>> findSocksByColorAndCottonPartAndSmaller(Color color, int cottonPart);

    Optional<Sock> findFirstByOrderByIdDesc();

}
