package ru.customer.service;

import ru.customer.model.SockResponse;

public interface SockService {

    SockResponse getAllSock(int pageNo, int pageSize, String sortBy, String sortDir);
}
