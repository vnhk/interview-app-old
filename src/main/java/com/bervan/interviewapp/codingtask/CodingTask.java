package com.bervan.interviewapp.codingtask;

import com.bervan.history.model.AbstractBaseEntity;
import com.bervan.history.model.HistoryCollection;
import com.bervan.history.model.HistorySupported;
import com.bervan.ieentities.ExcelIEEntity;
import com.bervan.interviewapp.model.PersistableTableData;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@HistorySupported
public class CodingTask implements AbstractBaseEntity<UUID>, PersistableTableData, ExcelIEEntity<UUID> {
    private String name;
    @Size(max = 50000)
    private String initialCode;
    @Size(max = 50000)
    private String exampleCode;
    @Size(max = 50000)
    private String exampleCodeDetails;
    @Size(max = 50000)
    private String questions;
    private LocalDateTime modificationDate;
    @Id
    @GeneratedValue
    private UUID id;

    @OneToMany(fetch = FetchType.EAGER)
    @HistoryCollection(historyClass = HistoryCodingTask.class)
    private Set<HistoryCodingTask> history = new HashSet<>();

    public CodingTask(String name, String initialCode, String exampleCode, String exampleCodeDetails, String questions, LocalDateTime modificationDate) {
        this.name = name;
        this.initialCode = initialCode;
        this.exampleCode = exampleCode;
        this.exampleCodeDetails = exampleCodeDetails;
        this.questions = questions;
        this.modificationDate = modificationDate;
    }

    public CodingTask() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInitialCode() {
        return initialCode;
    }

    public void setInitialCode(String initialCode) {
        this.initialCode = initialCode;
    }

    public String getExampleCode() {
        return exampleCode;
    }

    public void setExampleCode(String exampleCode) {
        this.exampleCode = exampleCode;
    }

    public String getExampleCodeDetails() {
        return exampleCodeDetails;
    }

    public void setExampleCodeDetails(String exampleCodeDetails) {
        this.exampleCodeDetails = exampleCodeDetails;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public LocalDateTime getModificationDate() {
        return modificationDate;
    }

    @Override
    public void setModificationDate(LocalDateTime modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Set<HistoryCodingTask> getHistory() {
        return history;
    }

    public void setHistory(Set<HistoryCodingTask> history) {
        this.history = history;
    }
}