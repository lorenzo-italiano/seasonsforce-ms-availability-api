package fr.polytech.service;

import fr.polytech.model.Availability;
import fr.polytech.model.AvailabilityDTO;
import fr.polytech.model.DetailedAvailabilityDTO;
import fr.polytech.model.JobCategoryDTO;
import fr.polytech.repository.AvailabilityRepository;
import jakarta.ws.rs.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

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

    @Autowired
    private JobCategoryService jobCategoryService;

    /**
     * Get all availabilities.
     *
     * @return List of all availabilities.
     */
    public List<Availability> getAllAvailabilities() {
        logger.info("Getting all availabilities");
        return availabilityRepository.findAll();
    }

    /**
     * Get availability by id.
     *
     * @param id Availability id.
     * @return Availability with the specified id.
     * @throws NotFoundException If the availability is not found.
     */
    public Availability getAvailabilityById(UUID id) throws HttpClientErrorException {
        logger.info("Getting availability with id " + id);
        Availability availability = availabilityRepository.findById(id).orElse(null);

        if (availability == null) {
            logger.error("Error while getting an availability: availability not found");
            // If the availability is not found, throw an exception
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Availability not found");
        }

        logger.debug("Returning availability with id " + id);
        return availability;
    }

    /**
     * Create an availability.
     *
     * @param availabilityDTO Availability to create.
     * @return Created availability.
     */
    public Availability createAvailability(AvailabilityDTO availabilityDTO) {
        logger.info("Creating availability");

        checkAttributes(availabilityDTO);

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

    /**
     * Update an availability.
     *
     * @param availabilityDTO Availability to update.
     * @return Updated availability.
     * @throws HttpClientErrorException If the availability is not found.
     */
    public Availability updateAvailability(AvailabilityDTO availabilityDTO) throws HttpClientErrorException {
        logger.info("Updating availability with id " + availabilityDTO.getId());

        checkAttributes(availabilityDTO);

        Availability availability = availabilityRepository.findById(availabilityDTO.getId()).orElse(null);

        if (availability == null) {
            logger.error("Error while getting an availability: availability not found");
            // If the availability is not found, throw an exception
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Availability not found");
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

    /**
     * Delete an availability.
     *
     * @param id Availability id.
     * @throws HttpClientErrorException If the availability is not found.
     */
    public void deleteAvailability(UUID id) throws HttpClientErrorException {
        logger.info("Deleting availability with id " + id);

        Availability availability = availabilityRepository.findById(id).orElse(null);

        if (availability == null) {
            logger.error("Error while getting an availability: availability not found");
            // If the availability is not found, throw an exception
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Availability not found");
        }

        availabilityRepository.deleteById(id);
        logger.debug("Deleted availability with id " + id);
    }

    /**
     * Check if the attributes of an availability are valid.
     *
     * @param availability Availability to check.
     * @throws HttpClientErrorException If the availability does not have all the required attributes.
     */
    private void checkAttributes(AvailabilityDTO availability) throws HttpClientErrorException {
        if (availability.getStartDate() == null || availability.getEndDate() == null || availability.getJobCategoryId() == null || availability.getJobTitle() == null || availability.getPlaceList() == null) {
            logger.error("Error while creating an availability: missing attributes");
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing attributes");
        }
        if (availability.getPlaceList().isEmpty()) {
            logger.error("Error while creating an availability: place list is empty");
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Place list is empty");
        }
        if (availability.getStartDate().after(availability.getEndDate())) {
            logger.error("Error while creating an availability: start date must be before end date");
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Start date must be before end date");
        }
    }

    /**
     * Get a detailed availability by id.
     *
     * @param id    Availability id.
     * @param token Token.
     * @return Detailed availability with the specified id.
     */
    public DetailedAvailabilityDTO getDetailedAvailabilityById(UUID id, String token) {
        Availability availability = availabilityRepository.findById(id).orElse(null);

        if (availability == null) {
            logger.error("Error while getting an availability: availability not found");
            // If the availability is not found, throw an exception
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Availability not found");
        }

        DetailedAvailabilityDTO detailedAvailabilityDTO = new DetailedAvailabilityDTO();
        detailedAvailabilityDTO.setId(availability.getId());
        detailedAvailabilityDTO.setJobTitle(availability.getJobTitle());
        detailedAvailabilityDTO.setEndDate(availability.getEndDate());
        detailedAvailabilityDTO.setStartDate(availability.getStartDate());
        detailedAvailabilityDTO.setPlaceList(availability.getPlaceList());

        JobCategoryDTO jobCategoryById = jobCategoryService.getJobCategoryById(availability.getJobCategoryId(), token);
        detailedAvailabilityDTO.setJobCategory(jobCategoryById);

        return detailedAvailabilityDTO;
    }
}
