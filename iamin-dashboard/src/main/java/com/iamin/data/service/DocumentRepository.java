package com.iamin.data.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iamin.data.entity.Document;
import com.iamin.data.entity.SamplePerson;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    @Query("SELECT d FROM Document d WHERE d.signed = true")
    List<Document> findBySignedTrue();
    List<Document> findBySignedFalse();
    List<Document> findByPerson(SamplePerson person);
}
