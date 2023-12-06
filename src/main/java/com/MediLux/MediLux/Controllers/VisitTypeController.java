package com.MediLux.MediLux.Controllers;

import com.MediLux.MediLux.Model.VisitType;
import com.MediLux.MediLux.Service.VisitTypeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/visit_types")
public class VisitTypeController {
    private final VisitTypeService visitTypeService;

    @GetMapping("")
    public ResponseEntity<List<VisitType>> getVisitTypes() {
        return new ResponseEntity<>(visitTypeService.findAllVisits(), HttpStatus.OK);
    }
}
