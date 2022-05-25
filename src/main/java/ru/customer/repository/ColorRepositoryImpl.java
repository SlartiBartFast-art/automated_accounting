package ru.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.customer.model.Color;

import java.util.Optional;

public interface ColorRepositoryImpl extends JpaRepository<Color, Long> {

    boolean existsColorByColoring(String coloring);

    Optional<Color> findColorByColoring(String coloring);

}
