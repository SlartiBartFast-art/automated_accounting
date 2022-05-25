package ru.customer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "colors")
public class Color {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String coloring;

//    @OneToMany(mappedBy = "color", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Sock> socks;

//    public Color(Long id, String coloring) {
//        this.id = id;
//        this.coloring = coloring;
//    }
}
