package com.iamin.data.entity;

import javax.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "document")
public class Document extends AbstractEntity {

    private String documentTitle;
    private String documentDescription;
    @Column(columnDefinition = "TEXT")
    private String documentUrl;
    private LocalDate uploadDate;
    private LocalDate submitDate;
    private Boolean signed;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private SamplePerson person;

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public String getDocumentDescription() {
        return documentDescription;
    }

    public void setDocumentDescription(String documentDescription) {
        this.documentDescription = documentDescription;
    }

    public String getDocumentUrl() {
        return documentUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }

    public LocalDate getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDate uploadDate) {
        this.uploadDate = uploadDate;
    }

    public LocalDate getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(LocalDate submitDate) {
        this.submitDate = submitDate;
    }

    public Boolean getSigned() {
        return signed;
    }

    public void setSigned(Boolean signed) {
        this.signed = signed;
    }

    public SamplePerson getPerson() {
        return person;
    }

    public void setPerson(SamplePerson person) {
        this.person = person;
    }
}

