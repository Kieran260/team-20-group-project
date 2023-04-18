package com.iamin.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.iamin.data.entity.Document;
import com.iamin.data.entity.SamplePerson;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;

    @Autowired
    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    
    public Document createDocument(Document document) {
        return documentRepository.save(document);
    }

    public Optional<Document> getDocumentById(Long documentId) {
        return documentRepository.findById(documentId);
    }

    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    public Document updateDocument(Document document) {
        return documentRepository.save(document);
    }

    public void deleteDocument(Long documentId) {
        documentRepository.deleteById(documentId);
    }


    public List<Document> getSignedDocuments() {
        return documentRepository.findBySignedTrue();
    }
    public List<Document> findDocumentsForPerson(SamplePerson person) {
        return documentRepository.findByPerson(person);
    }

    public List<Document> getUnsignedDocuments () {
        return documentRepository.findBySignedFalse();
    }
    
}
