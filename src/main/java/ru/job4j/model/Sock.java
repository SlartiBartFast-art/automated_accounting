package ru.job4j.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * модель описывает Носок -
 * вид одежды для нижней части ног — короткий чулок, верхний край которого не достигает колена
 * color — цвет носков, строка (например, black, red, yellow);
 * cottonPart — процентное содержание хлопка в составе носков, целое число от 0 до 100 (например, 30, 18, 42);
 * quantity — количество пар носков, целое число больше 0.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "socks")
public class Sock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int cottonPart;
    private int quantity;

    @OneToOne
    @JoinColumn(name = "color_id")
    private Color color;

    public static Sock of(Color color, int cottonPart, int quantity) {
        Sock sock = new Sock();
        sock.color = color;
        sock.cottonPart = cottonPart;
        sock.quantity = quantity;
        return sock;
    }
}
