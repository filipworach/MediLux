package com.MediLux.MediLux.Service;

import com.MediLux.MediLux.Model.Status;
import com.MediLux.MediLux.Repositories.StatusRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StatusService {
    StatusRepository statusRepository;
    public List<Status> findAll() {
        return statusRepository.findAll();
    }
}
