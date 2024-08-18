package com.bervan.interviewapp.onevalue;

import com.bervan.history.model.BaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface OneValueRepository extends BaseRepository<OneValue, UUID> {
    Optional<OneValue> findByName(String name);
}
