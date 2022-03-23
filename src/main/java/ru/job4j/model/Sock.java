package ru.job4j.model;

import lombok.Data;

import javax.persistence.*;

/**
 * модель описывает Носок -
 * вид одежды для нижней части ног — короткий чулок, верхний край которого не достигает колена
 * color — цвет носков, строка (например, black, red, yellow);
 * cottonPart — процентное содержание хлопка в составе носков, целое число от 0 до 100 (например, 30, 18, 42);
 * quantity — количество пар носков, целое число больше 0.
 */

@Data
@Entity
@Table(name = "socks")
public class Sock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String color;
    private int cottonPart;
    private int quantity;

    public Sock() {
    }

    public static Sock of(String color, int cottonPart, int quantity) {
        Sock sock = new Sock();
        sock.color = color;
        sock.cottonPart = cottonPart;
        sock.quantity = quantity;
        return sock;
    }
}
