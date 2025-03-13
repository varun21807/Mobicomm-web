package com.mobicomm.app.service;

import com.mobicomm.app.model.Ott;
import com.mobicomm.app.repository.OttRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OttService {

    @Autowired
    private OttRepository ottRepository;

    // ✅ Generate next OTT ID in format "ott_1", "ott_2", "ott_3"
    private String generateOttId() {
        Optional<Ott> lastOtt = ottRepository.findTopByOrderByOttIdDesc(); // ✅ Use Optional

        if (lastOtt.isPresent()) {
            String lastId = lastOtt.get().getOttId(); // e.g., "ott_10"
            int lastNumber = Integer.parseInt(lastId.replace("ott_", "")); // Extract numeric part
            return "ott_" + (lastNumber + 1); // ✅ Generate new ID
        } else {
            return "ott_1"; // ✅ First record
        }
    }


    public List<Ott> getAllOtts() {
        return ottRepository.findAll();
    }

    public Optional<Ott> getOttById(String ottId) {
        return ottRepository.findById(ottId);
    }

    public Ott addOtt(Ott ott) {
        ott.setOttId(generateOttId()); // ✅ Assign generated ID before saving
        return ottRepository.save(ott);
    }


    public Ott updateOtt(String ottId, Ott updatedOtt) {
        return ottRepository.findById(ottId)
            .map(existingOtt -> {
                existingOtt.setOttName(updatedOtt.getOttName());
                existingOtt.setIconImg(updatedOtt.getIconImg());
                return ottRepository.save(existingOtt);
            })
            .orElseThrow(() -> new RuntimeException("OTT not found with ID: " + ottId));
    }

    public void deleteOtt(String ottId) {
        ottRepository.deleteById(ottId);
    }
}
