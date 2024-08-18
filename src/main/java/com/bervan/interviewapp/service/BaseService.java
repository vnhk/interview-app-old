package com.bervan.interviewapp.service;

import com.bervan.interviewapp.model.PersistableTableData;

import java.util.List;

public interface BaseService<T extends PersistableTableData> {
    void save(List<T> data);

    void save(T data);

    List<T> load();

}
