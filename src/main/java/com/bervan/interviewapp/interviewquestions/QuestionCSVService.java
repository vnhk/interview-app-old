package com.bervan.interviewapp.interviewquestions;

import com.bervan.interviewapp.service.CSVService;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class QuestionCSVService implements CSVService<Question> {

    @Override
    public void save(List<Question> questions) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        String fileName = "db/InterviewQuestions_T" + timestamp + ".csv";

        try (CSVWriter writer = new CSVWriter(new FileWriter(fileName))) {
            String[] header = {"Name", "Tags", "Difficulty", "Question Details", "Answer Details", "Max Points"};
            writer.writeNext(header);

            for (Question question : questions) {
                String[] data = {
                        question.getName(),
                        question.getTags(),
                        String.valueOf(question.getDifficulty()),
                        question.getQuestionDetails(),
                        question.getAnswerDetails(),
                        String.valueOf(question.getMaxPoints())
                };
                writer.writeNext(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(Question data) {

    }

    @Override
    public List<Question> load() {
        File directory = new File("db");
        File[] files = directory.listFiles((dir, name) -> name.startsWith("InterviewQuestions_T") && name.endsWith(".csv"));

        if (files != null && files.length > 0) {
            // Sort files by timestamp in the name (descending order)
            Arrays.sort(files, (f1, f2) -> {
                String timestamp1 = f1.getName().substring("InterviewQuestions_T".length(), f1.getName().length() - 4);
                String timestamp2 = f2.getName().substring("InterviewQuestions_T".length(), f2.getName().length() - 4);
                return timestamp2.compareTo(timestamp1);
            });

            // Load the latest file
            File latestFile = files[0];
            return loadQuestionsFromCSV(latestFile.getPath());
        } else {
            return loadQuestionsFromCSV("db/InterviewQuestions_db.csv");
        }
    }

    private List<Question> loadQuestionsFromCSV(String filePath) {
        List<Question> questions = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(filePath)))) {
            String[] line;
            reader.readNext();

            while ((line = reader.readNext()) != null) {
                String name = line[0];
                String tags = line[1];
                int difficulty = Integer.parseInt(line[2]);
                String questionDetails = line[3];
                String answerDetails = line[4];
                double maxPoints = Double.parseDouble(line[5]);

                questions.add(new Question(name, tags, difficulty, questionDetails, answerDetails, maxPoints));
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException("Loading Failed");
        }

        return questions;
    }
}
