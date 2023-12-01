package com.MediLux.MediLux.Service;

import com.MediLux.MediLux.Model.VisitType;
import com.MediLux.MediLux.Repositories.VisitTypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class VisitTypeService {
    private final VisitTypeRepository visitTypeRepository;

    public List<VisitType> findAllVisits() {
        return visitTypeRepository.findAll();
    }
}
