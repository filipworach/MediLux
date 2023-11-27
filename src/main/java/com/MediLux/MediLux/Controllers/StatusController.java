package com.MediLux.MediLux.Controllers;

import com.MediLux.MediLux.Model.Status;
import com.MediLux.MediLux.Service.StatusService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/status")
@AllArgsConstructor
public class StatusController {
    StatusService statusService;
    @GetMapping("")
    public ResponseEntity<List<Status>> findAll() {
        return new ResponseEntity<>(statusService.findAll(), HttpStatus.OK);
    }
}
