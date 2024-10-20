package com.tadayon.csvexporter.repository;

import com.tadayon.csvexporter.model.BlackList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

@org.springframework.stereotype.Repository
public interface BlackListRepository extends JpaRepository<BlackList, String> {
    Page<BlackList> findAllByOperationDateGreaterThanEqualOrderByOperationDateAsc(LocalDateTime time, Pageable pageable);
}
