package fr.polytech.service;

import fr.polytech.model.Availability;
import fr.polytech.model.AvailabilityDTO;
import fr.polytech.repository.AvailabilityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class AvailabilityServiceTest {

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Autowired
    private AvailabilityService availabilityService;

    @Test
    public void testGetAllAvailabilities() {
        availabilityRepository.save(new Availability()); // Save some dummy data
        availabilityRepository.save(new Availability());

        List<Availability> result = availabilityService.getAllAvailabilities();
        assertNotNull(result);
        // Check that there are at least two availabilities
        assertTrue(result.size() >= 2);
    }

    @Test
    public void testGetAvailabilityById() {
        Availability savedAvailability = availabilityRepository.save(new Availability());

        Availability result = availabilityService.getAvailabilityById(savedAvailability.getId());
        assertNotNull(result);
        assertEquals(savedAvailability.getId(), result.getId());
    }

    @Test
    public void testGetAvailabilityByIdWithInvalidId() {
        // Check that an exception is thrown with status code 404
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> availabilityService.getAvailabilityById(UUID.randomUUID()));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    public void testCreateAvailability() {
        AvailabilityDTO availability = new AvailabilityDTO();
        availability.setStartDate(new Date());
        availability.setEndDate(new Date());
        availability.setJobCategoryId(UUID.randomUUID());
        availability.setJobTitle("jobTitle");
        availability.setPlaceList(List.of("place1", "place2"));

        Availability result = availabilityService.createAvailability(availability);
        assertNotNull(result);
        assertNotNull(result.getId());
    }

    @Test
    public void testCreateAvailabilityWithEndDateBeforeStartDate() {
        AvailabilityDTO availability = new AvailabilityDTO();
        availability.setJobCategoryId(UUID.randomUUID());
        availability.setJobTitle("jobTitle");
        availability.setPlaceList(List.of("place1", "place2"));
        availability.setStartDate(new Date());
        // Set the end date to one second before the start date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(availability.getStartDate());
        calendar.add(Calendar.SECOND, -1);
        availability.setEndDate(calendar.getTime());

        // Check that an exception is thrown with status code 400
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> availabilityService.createAvailability(availability));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    public void testCreateAvailabilityWithMissingAttributes() {
        AvailabilityDTO availability = new AvailabilityDTO();
        availability.setStartDate(new Date());

        // Check that an exception is thrown with status code 400
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> availabilityService.createAvailability(availability));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    public void testUpdateAvailability() {
        Availability availabilityToSave = new Availability();
        availabilityToSave.setJobCategoryId(UUID.randomUUID());
        availabilityToSave.setJobTitle("jobTitle");
        availabilityToSave.setPlaceList(List.of("place1", "place2"));
        availabilityToSave.setStartDate(new Date());
        availabilityToSave.setEndDate(new Date());
        Availability savedAvailability = availabilityRepository.save(availabilityToSave);

        AvailabilityDTO availability = new AvailabilityDTO();
        availability.setJobCategoryId(UUID.randomUUID());
        availability.setJobTitle("jobTitle");
        availability.setPlaceList(List.of("place1"));
        availability.setId(savedAvailability.getId());
        availability.setStartDate(new Date());
        availability.setEndDate(new Date());

        Availability result = availabilityService.updateAvailability(availability);
        assertNotNull(result);
        assertEquals(savedAvailability.getId(), result.getId());
    }

    @Test
    public void testUpdateAvailabilityWithEndDateBeforeStartDate() {
        Availability availabilityToSave = new Availability();
        availabilityToSave.setJobCategoryId(UUID.randomUUID());
        availabilityToSave.setJobTitle("jobTitle");
        availabilityToSave.setPlaceList(List.of("place1", "place2"));
        availabilityToSave.setStartDate(new Date());
        availabilityToSave.setEndDate(new Date());
        Availability savedAvailability = availabilityRepository.save(availabilityToSave);

        AvailabilityDTO availability = new AvailabilityDTO();
        availability.setJobCategoryId(UUID.randomUUID());
        availability.setJobTitle("jobTitle");
        availability.setPlaceList(List.of("place1"));
        availability.setId(savedAvailability.getId());
        availability.setStartDate(new Date());
        // Set the end date to one second before the start date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(availability.getStartDate());
        calendar.add(Calendar.SECOND, -1);
        availability.setEndDate(calendar.getTime());

        // Check that an exception is thrown with status code 400
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> availabilityService.updateAvailability(availability));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    public void testUpdateAvailabilityWithMissingAttributes() {
        Availability availabilityToSave = new Availability();
        availabilityToSave.setJobCategoryId(UUID.randomUUID());
        availabilityToSave.setJobTitle("jobTitle");
        availabilityToSave.setPlaceList(List.of("place1", "place2"));
        availabilityToSave.setStartDate(new Date());
        availabilityToSave.setEndDate(new Date());
        Availability savedAvailability = availabilityRepository.save(availabilityToSave);

        AvailabilityDTO availability = new AvailabilityDTO();
        availability.setId(savedAvailability.getId());
        availability.setStartDate(new Date());

        // Check that an exception is thrown with status code 400
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> availabilityService.updateAvailability(availability));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    public void testUpdateAvailabilityWithInvalidId() {
        AvailabilityDTO availability = new AvailabilityDTO();
        availability.setId(UUID.randomUUID());
        availability.setStartDate(new Date());
        availability.setEndDate(new Date());
        availability.setJobCategoryId(UUID.randomUUID());
        availability.setJobTitle("jobTitle");
        availability.setPlaceList(List.of("place1", "place2"));

        // Check that an exception is thrown with status code 404
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> availabilityService.updateAvailability(availability));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    public void testDeleteAvailability() {
        Availability savedAvailability = availabilityRepository.save(new Availability());

        availabilityService.deleteAvailability(savedAvailability.getId());

        // Check that the availability has been deleted
        assertFalse(availabilityRepository.findById(savedAvailability.getId()).isPresent());
    }

}
