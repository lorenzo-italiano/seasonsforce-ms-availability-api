package fr.polytech.service;

import fr.polytech.model.Availability;
import fr.polytech.model.AvailabilityDTO;
import fr.polytech.repository.AvailabilityRepository;
import jakarta.ws.rs.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AvailabilityService {

    /**
     * Initialize the logger.
     */
    private final Logger logger = LoggerFactory.getLogger(AvailabilityService.class);

    @Autowired
    private AvailabilityRepository availabilityRepository;

    public List<Availability> getAllAvailabilities() {
        logger.info("Getting all availabilities");
        return availabilityRepository.findAll();
    }

    public Availability getAvailabilityById(UUID id) throws NotFoundException {
        logger.info("Getting availability with id " + id);
        Availability availability = availabilityRepository.findById(id).orElse(null);

        if (availability == null) {
            logger.error("Error while getting an availability: availability not found");
            // If the availability is not found, throw an exception
            throw new NotFoundException("Availability not found");
        }

        logger.debug("Returning availability with id " + id);
        return availability;
    }

    public Availability createAvailability(AvailabilityDTO availabilityDTO) {
        logger.info("Creating availability");

        Availability availability = new Availability();
        availability.setStartDate(availabilityDTO.getStartDate());
        availability.setEndDate(availabilityDTO.getEndDate());
        availability.setJobCategoryId(availabilityDTO.getJobCategoryId());
        availability.setJobTitle(availabilityDTO.getJobTitle());
        availability.setPlaceList(availabilityDTO.getPlaceList());

        availabilityRepository.save(availability);
        logger.debug("Created availability with id " + availability.getId());
        return availability;
    }

    public Availability updateAvailability(AvailabilityDTO availabilityDTO) throws NotFoundException {
        logger.info("Updating availability with id " + availabilityDTO.getId());

        Availability availability = availabilityRepository.findById(availabilityDTO.getId()).orElse(null);

        if (availability == null) {
            logger.error("Error while getting an availability: availability not found");
            // If the availability is not found, throw an exception
            throw new NotFoundException("Availability not found");
        }

        availability.setStartDate(availabilityDTO.getStartDate());
        availability.setEndDate(availabilityDTO.getEndDate());
        availability.setJobCategoryId(availabilityDTO.getJobCategoryId());
        availability.setJobTitle(availabilityDTO.getJobTitle());
        availability.setPlaceList(availabilityDTO.getPlaceList());

        availabilityRepository.save(availability);
        logger.debug("Updated availability with id " + availability.getId());
        return availability;
    }

    public void deleteAvailability(UUID id) throws NotFoundException {
        logger.info("Deleting availability with id " + id);

        Availability availability = availabilityRepository.findById(id).orElse(null);

        if (availability == null) {
            logger.error("Error while getting an availability: availability not found");
            // If the availability is not found, throw an exception
            throw new NotFoundException("Availability not found");
        }

        availabilityRepository.deleteById(id);
        logger.debug("Deleted availability with id " + id);
    }
}
