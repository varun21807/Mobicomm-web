package com.mobicomm.app.controller;

import com.mobicomm.app.model.Ott;
import com.mobicomm.app.service.OttService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://127.0.0.1:5503")
@RestController
@RequestMapping("/api/ott")
public class OttController {

    @Autowired
    private OttService ottService;

    // ✅ Public: Any user can view OTT platforms
    @GetMapping
    public List<Ott> getAllOtts() {
        return ottService.getAllOtts();
    }

    // ✅ Public: Any user can view a specific OTT platform
    @GetMapping("/{id}")
    public Optional<Ott> getOttById(@PathVariable String id) {
        return ottService.getOttById(id);
    }

    // ✅ Only Admins Can Add OTT Services
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public Ott addOtt(@RequestBody Ott ott) {
        return ottService.addOtt(ott);
    }

    // ✅ Only Admins Can Update OTT Services
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public Ott updateOtt(@PathVariable String id, @RequestBody Ott updatedOtt) {
        return ottService.updateOtt(id, updatedOtt);
    }

    // ✅ Only Admins Can Delete OTT Services
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteOtt(@PathVariable String id) {
        ottService.deleteOtt(id);
    }
}
