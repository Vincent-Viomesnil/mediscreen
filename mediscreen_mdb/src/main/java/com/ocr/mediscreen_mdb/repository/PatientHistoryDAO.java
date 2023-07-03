package com.ocr.mediscreen_mdb.repository;

import com.ocr.mediscreen_mdb.model.PatientHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientHistoryDAO extends MongoRepository<PatientHistory, String> {
    List<PatientHistory> findByPatId(Long patId);

    Optional<PatientHistory> findByNoteId(Long noteId);

    void deleteByNoteId(Long noteId);

    PatientHistory findFirstByOrderByNoteIdDesc();
}
