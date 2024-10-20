package com.tadayon.csvexporter.service;

import com.tadayon.csvexporter.config.CsvConfig;
import com.tadayon.csvexporter.constant.CsvConstant;
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
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CsvExporterService {
    private final BlackListRepository blackListRepository;
    private final CsvConfig csvConfig;

    @PostConstruct
    public void init(){
        log.info("Initializing CSV export");
        try {
            exportToCsv(csvConfig.getFilePath());
        } catch (IOException e) {
            log.error("Failed to export CSV", e);
        }
    }

    public void exportToCsv(String filePath) throws IOException {
        long startTime = System.currentTimeMillis();
        log.info("Export started at: {}", startTime);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(CsvConstant.HEADER);

            int pageNumber = 0;
            Page<BlackList> page;
            Timestamp timestamp = new Timestamp(0);

            do {
                page = fetchPage(timestamp.toLocalDateTime(), pageNumber);
                String csvContent = convertPageToCsv(page);
                writer.write(csvContent);
                writer.flush();

                log.info("Processed page: {}", pageNumber);
                pageNumber++;

            } while (page.hasNext());

            long endTime = System.currentTimeMillis();
            log.info("Export completed at: {}. Duration: {} ms", endTime, endTime - startTime);
        }
    }

    private Page<BlackList> fetchPage(LocalDateTime timestamp, int pageNumber) {
        return blackListRepository
                .findAllByOperationDateGreaterThanEqualOrderByOperationDateAsc(
                        timestamp,
                        PageRequest.of(
                                pageNumber,
                                csvConfig.getBatchSize())
                );
    }

    private String convertPageToCsv(Page<BlackList> page) {
        return page.getContent().stream()
                .map(this::recordToCsvLine)
                .collect(Collectors.joining());
    }

    private String recordToCsvLine(BlackList blackList) {
        return String.format(CsvConstant.FORMAT, blackList.getCardId(), blackList.getOperationDate());
    }
}
