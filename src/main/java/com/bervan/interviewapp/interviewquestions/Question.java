package com.bervan.interviewapp.interviewquestions;

import com.bervan.history.model.AbstractBaseEntity;
import com.bervan.history.model.HistoryCollection;
import com.bervan.history.model.HistorySupported;
import com.bervan.ieentities.ExcelIEEntity;
import com.bervan.interviewapp.model.PersistableTableData;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@HistorySupported
public class Question implements AbstractBaseEntity<UUID>, PersistableTableData, ExcelIEEntity<UUID> {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String tags;
    @Min(1)
    @Max(5)
    private Integer difficulty;
    @Size(max = 50000)
    private String questionDetails;
    @Size(max = 50000)
    private String answerDetails;
    private Double maxPoints;
    private LocalDateTime modificationDate;

    @OneToMany(fetch = FetchType.EAGER)
    @HistoryCollection(historyClass = HistoryQuestion.class)
    private Set<HistoryQuestion> history = new HashSet<>();


    public Question(String name, String tags, int difficulty, String questionDetails, String answerDetails, double maxPoints) {
        this.name = name;
        this.tags = tags;
        this.difficulty = difficulty;
        this.questionDetails = questionDetails;
        this.answerDetails = answerDetails;
        this.maxPoints = maxPoints;
    }

    public Question() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public String getQuestionDetails() {
        return questionDetails;
    }

    public void setQuestionDetails(String questionDetails) {
        this.questionDetails = questionDetails;
    }

    public String getAnswerDetails() {
        return answerDetails;
    }

    public void setAnswerDetails(String answerDetails) {
        this.answerDetails = answerDetails;
    }

    public double getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(double maxPoints) {
        this.maxPoints = maxPoints;
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

    public Set<HistoryQuestion> getHistory() {
        return history;
    }

    public void setHistory(Set<HistoryQuestion> history) {
        this.history = history;
    }
}