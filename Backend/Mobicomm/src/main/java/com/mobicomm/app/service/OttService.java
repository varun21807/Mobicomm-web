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

    public String generateOttId() {
        // Fetch the latest OTT entry in descending order
        Ott latestOtt = ottRepository.findTopByOrderByOttIdDesc();

        // Default to 1 if no OTT exists
        int nextId = 1;
        if (latestOtt != null) {
            String latestOttId = latestOtt.getOttId(); // Expected format: MCO-XXXX
            String numberPart = latestOttId.substring(4); // Extract numeric part

            try {
                nextId = Integer.parseInt(numberPart) + 1; // Increment number part
            } catch (NumberFormatException e) {
                nextId = 1;
            }
        }

        // Generate the new OTT ID
        return String.format("MCO-%04d", nextId);
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
