package com.school_management.service;

import java.util.List;

import com.school_management.dao.SubjectDAO;
import com.school_management.model.Subject;

public class SubjectService {

    private final SubjectDAO subjectRepository = new SubjectDAO();

    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public String registerSubject(Subject subject) {
        if (subject.getSubjectName() == null || subject.getSubjectName().trim().isEmpty()) {
            return "Error: Subject name cannot be blank.";
        }

        if (subjectRepository.existsBySubjectName(subject.getSubjectName().trim())) {
            return "Error: A subject named '" + subject.getSubjectName() + "' already exists.";
        }

        subject.setSubjectName(subject.getSubjectName().trim());
        subjectRepository.save(subject);
        return "Success";
    }
}