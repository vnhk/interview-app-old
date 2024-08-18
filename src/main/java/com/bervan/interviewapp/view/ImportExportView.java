package com.bervan.interviewapp.view;

import com.bervan.ieentities.BaseExcelExport;
import com.bervan.ieentities.BaseExcelImport;
import com.bervan.ieentities.ExcelIEEntity;
import com.bervan.ieentities.LoadIEAvailableEntities;
import com.bervan.interviewapp.codingtask.CodingTaskService;
import com.bervan.interviewapp.interviewquestions.InterviewQuestionService;
import com.bervan.interviewapp.onevalue.OneValueService;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.bervan.interviewapp.view.ImportExportView.ROUTE_NAME;

@Route(ROUTE_NAME)
public class ImportExportView extends PageLayout {
    @PersistenceContext
    private EntityManager entityManager;
    public static final String ROUTE_NAME = "import-export-data";
    private final CodingTaskService codingTaskService;
    private final OneValueService oneValueService;
    private final InterviewQuestionService interviewQuestionService;

    public ImportExportView(CodingTaskService codingTaskService, OneValueService oneValueService, InterviewQuestionService interviewQuestionService) {
        super(ROUTE_NAME);
        this.codingTaskService = codingTaskService;
        this.oneValueService = oneValueService;
        this.interviewQuestionService = interviewQuestionService;

        try {
            StreamResource resource = prepareDownloadResource();
            Anchor downloadLink = new Anchor(resource, "Export");
            downloadLink.getElement().setAttribute("download", true);

            add(downloadLink);
        } catch (IOException e) {
            e.printStackTrace();
        }


        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);

        upload.addSucceededListener(event -> {
            String fileName = event.getFileName();
            InputStream inputStream = buffer.getInputStream();
            try {
                saveFile(inputStream, fileName);
                Notification.show("File uploaded successfully: " + fileName);
            } catch (Exception e) {
                e.printStackTrace();
                Notification.show("Failed to upload file: " + fileName);
            }
        });

        upload.addFailedListener(event -> {
            Notification.show("Upload failed");
        });

        add(upload);

    }

    private void saveFile(InputStream inputStream, String fileName) throws IOException {
        String uploadDir = "./tmp/";
        File uploadFolder = new File(uploadDir);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }
        File file = new File(uploadDir + fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }

        LoadIEAvailableEntities loadIEAvailableEntities = new LoadIEAvailableEntities();
        BaseExcelImport baseExcelImport = new BaseExcelImport(loadIEAvailableEntities.getSubclassesOfExcelEntity("com.bervan.interviewapp")
                .stream().filter(e -> !e.getName().contains("History")).collect(Collectors.toList()));
        List<? extends ExcelIEEntity> objects = (List<? extends ExcelIEEntity>) baseExcelImport.importExcel(baseExcelImport.load(file));
        codingTaskService.saveIfValid(objects);
        oneValueService.saveIfValid(objects);
        interviewQuestionService.saveIfValid(objects);
    }


    public StreamResource prepareDownloadResource() throws IOException {
        BaseExcelExport baseExcelExport = new BaseExcelExport();
        Workbook workbook = baseExcelExport.exportExcel(getDataToExport(), null);
        File saved = baseExcelExport.save(workbook, "./tmp", "export" + LocalDateTime.now() + ".xlsx");
        String filename = saved.getName();

        return new StreamResource(filename, () -> {
            try {
                return new FileInputStream(saved);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    private List<ExcelIEEntity<?>> getDataToExport() {
        List<ExcelIEEntity<?>> result = new ArrayList<>();
        result.addAll(codingTaskService.load());
        result.addAll(interviewQuestionService.load());
        result.addAll(oneValueService.load());
        return result;
    }
}
