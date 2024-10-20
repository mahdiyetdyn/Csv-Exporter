package com.tadayon.csvexporter.service;

import com.tadayon.csvexporter.config.CsvConfig;
import com.tadayon.csvexporter.model.BlackList;
import com.tadayon.csvexporter.repository.BlackListRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CsvExporterService {
    private final BlackListRepository blackListRepository;
    private final CsvConfig csvConfig;

    @PostConstruct
    public void init() throws IOException {
        log.info("in post construct");
        exportToCsv(csvConfig.getFilePath());
    }

    public void exportToCsv(String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            //Write CSV header
            writer.write("card_id,operation_date\n");

            int pageNumber = 0;
            boolean hasMoreRecords = true;
            long startTime, endTime;

            log.info("start time: {}", startTime = System.currentTimeMillis());
            Timestamp timestamp = new Timestamp(0);
            while (hasMoreRecords) {
                // Fetch pages
                Page<BlackList> page = blackListRepository
                        .findAllByOperationDateGreaterThanEqualOrderByOperationDateAsc(
                                timestamp.toLocalDateTime(),
                                PageRequest.of(pageNumber, csvConfig.getBatchSize()));

                List<BlackList> blackLists = page.getContent();

                // Batch Write Records
                StringBuilder sb = new StringBuilder();
                for (BlackList blackList : blackLists) {
                    // Write each record as a CSV line
                    sb.append(recordToCsvLine(blackList)).append("\n");
                }
                writer.write(sb.toString());
                writer.flush();

                hasMoreRecords = page.hasNext();
                pageNumber++;
            }
            log.info("end time: {}", endTime = System.currentTimeMillis());
            log.info("duration: {}", endTime - startTime);
        }
    }

    private String recordToCsvLine(BlackList blackList) {
        return String.format("%s,%s", blackList.getCardId(), blackList.getOperationDate());
    }
}
