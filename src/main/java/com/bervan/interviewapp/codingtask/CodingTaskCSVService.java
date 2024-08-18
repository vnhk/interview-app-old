package com.bervan.interviewapp.codingtask;

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
public class CodingTaskCSVService implements CSVService<CodingTask> {

    @Override
    public void save(List<CodingTask> tasks) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        String fileName = "db/CodingTasks_T" + timestamp + ".csv";

        try (CSVWriter writer = new CSVWriter(new FileWriter(fileName))) {
            String[] header = {"Name", "Initial Code", "Example Code", "Example Code Details", "Questions"};
            writer.writeNext(header);

            for (CodingTask task : tasks) {
                String[] data = {
                        task.getName(),
                        task.getInitialCode(),
                        task.getExampleCode(),
                        task.getExampleCodeDetails(),
                        task.getQuestions()
                };
                writer.writeNext(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(CodingTask data) {

    }

    @Override
    public List<CodingTask> load() {
        File directory = new File("db");
        File[] files = directory.listFiles((dir, name) -> name.startsWith("CodingTasks_T") && name.endsWith(".csv"));

        if (files != null && files.length > 0) {
            // Sort files by timestamp in the name (descending order)
            Arrays.sort(files, (f1, f2) -> {
                String timestamp1 = f1.getName().substring("CodingTasks_T".length(), f1.getName().length() - 4);
                String timestamp2 = f2.getName().substring("CodingTasks_T".length(), f2.getName().length() - 4);
                return timestamp2.compareTo(timestamp1);
            });

            // Load the latest file
            File latestFile = files[0];
            return loadCodingTasksFromCSV(latestFile.getPath());
        } else {
            return loadCodingTasksFromCSV("db/CodingTasks_db.csv");
        }
    }

    private List<CodingTask> loadCodingTasksFromCSV(String filePath) {
        List<CodingTask> tasks = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(filePath)))) {
            String[] line;
            reader.readNext();

            while ((line = reader.readNext()) != null) {
                String name = line[0];
                String initialCode = line[1];
                String exampleCode = line[2];
                String exampleCodeDetails = line[3];
                String questions = line[4];

                tasks.add(new CodingTask(name, initialCode, exampleCode, exampleCodeDetails, questions, LocalDateTime.now()));
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException("Loading Failed");
        }

        return tasks;
    }

}
