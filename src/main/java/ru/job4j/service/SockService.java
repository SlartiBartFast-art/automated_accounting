package ru.job4j.service;

import ru.job4j.model.SockResponse;

public interface SockService {

    SockResponse getAllSock(int pageNo, int pageSize, String sortBy, String sortDir);
}
