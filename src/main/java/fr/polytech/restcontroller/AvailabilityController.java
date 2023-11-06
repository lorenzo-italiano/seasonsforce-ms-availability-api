package fr.polytech.restcontroller;

import fr.polytech.model.Availability;
import fr.polytech.model.AvailabilityDTO;
import fr.polytech.service.AvailabilityService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Produces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    /**
     * Get all availabilities.
     *
     * @return List of all availabilities.
     */
    @GetMapping("/")
    @PreAuthorize("hasRole('client_admin')")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
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

    /**
     * Get availability by id.
     *
     * @param id Availability id.
     * @return Availability with the specified id.
     */
    @GetMapping("/{id}")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
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

    /**
     * Create an availability.
     *
     * @param availabilityDTO Availability to create.
     * @return Created availability.
     */
    @PostMapping("/")
    @PreAuthorize("hasRole('client_candidate')")
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
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

    /**
     * Update an availability.
     *
     * @param availabilityDTO Availability to update.
     * @return Updated availability.
     */
    @PutMapping("/")
    @PreAuthorize("hasRole('client_candidate')")
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
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

    /**
     * Delete an availability.
     *
     * @param id Availability id.
     * @return True if the availability has been deleted, false otherwise.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('client_candidate')")
    @Produces(MediaType.TEXT_PLAIN_VALUE)
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
