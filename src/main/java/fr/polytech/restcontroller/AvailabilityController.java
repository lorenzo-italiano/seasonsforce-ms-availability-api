package fr.polytech.restcontroller;

import fr.polytech.model.Availability;
import fr.polytech.model.AvailabilityDTO;
import fr.polytech.service.AvailabilityService;
import jakarta.ws.rs.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/availability")
public class AvailabilityController {

    /**
     * Initialize the logger.
     */
    private final Logger logger = LoggerFactory.getLogger(AvailabilityController.class);

    @Autowired
    private AvailabilityService availabilityService;

    @GetMapping("/")
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity<List<Availability>> getAllAvailabilities() {
        try {
            List<Availability> availabilities = availabilityService.getAllAvailabilities();
            logger.info("Got all availabilities");
            return ResponseEntity.ok(availabilities);
        } catch (Exception e) {
            logger.error("Error while getting all availabilities: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Availability> getAvailabilityById(@PathVariable("id") UUID id) {
        try {
            Availability availability = availabilityService.getAvailabilityById(id);
            logger.info("Got availability with id " + id);
            return ResponseEntity.ok(availability);
        } catch (NotFoundException e) {
            logger.error("Error while getting availability with id " + id + ": " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/")
    @PreAuthorize("hasRole('client_candidate')")
    public ResponseEntity<Availability> createAvailability(@RequestBody AvailabilityDTO availabilityDTO) {
        try {
            Availability availability = availabilityService.createAvailability(availabilityDTO);
            logger.info("Created availability with id " + availability.getId());
            return ResponseEntity.ok(availability);
        } catch (Exception e) {
            logger.error("Error while creating availability: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/")
    @PreAuthorize("hasRole('client_candidate')")
    public ResponseEntity<Availability> updateAvailability(@RequestBody AvailabilityDTO availabilityDTO) {
        try {
            Availability availability = availabilityService.updateAvailability(availabilityDTO);
            logger.info("Updated availability with id " + availability.getId());
            return ResponseEntity.ok(availability);
        } catch (Exception e) {
            logger.error("Error while updating availability: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('client_candidate')")
    public ResponseEntity<Boolean> deleteAvailability(@PathVariable("id") UUID id) {
        try {
            availabilityService.deleteAvailability(id);
            logger.info("Deleted availability with id " + id);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            logger.error("Error while deleting availability: " + e.getMessage());
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
    }
}
