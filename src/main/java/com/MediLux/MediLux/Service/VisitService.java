package com.MediLux.MediLux.Service;

import com.MediLux.MediLux.Exceptions.NotFoundException;
import com.MediLux.MediLux.Model.Visit;
import com.MediLux.MediLux.Repositories.VisitRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class VisitService {
    private final VisitRepository visitRepository;
    public List<Visit> findByPatientEmail(String email) throws NotFoundException {
        return visitRepository.findByPatientEmail(email);
    }
}
