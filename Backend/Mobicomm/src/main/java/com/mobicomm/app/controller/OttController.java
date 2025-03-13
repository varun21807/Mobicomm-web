package com.mobicomm.app.controller;

import com.mobicomm.app.model.Ott;
import com.mobicomm.app.service.OttService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ott")
public class OttController {

    @Autowired
    private OttService ottService;

    @GetMapping
    public List<Ott> getAllOtts() {
        return ottService.getAllOtts();
    }

    @GetMapping("/{id}")
    public Optional<Ott> getOttById(@PathVariable String id) {
        return ottService.getOttById(id);
    }

    @PostMapping
    public Ott addOtt(@RequestBody Ott ott) {
        return ottService.addOtt(ott);
    }

    @PutMapping("/{id}")
    public Ott updateOtt(@PathVariable String id, @RequestBody Ott updatedOtt) {
        return ottService.updateOtt(id, updatedOtt);
    }

    @DeleteMapping("/{id}")
    public void deleteOtt(@PathVariable String id) {
        ottService.deleteOtt(id);
    }
}
