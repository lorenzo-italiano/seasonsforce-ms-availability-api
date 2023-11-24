package fr.polytech.restcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.polytech.model.Availability;
import fr.polytech.model.AvailabilityDTO;
import fr.polytech.service.AvailabilityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = AvailabilityController.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
public class AvailabilityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AvailabilityService availabilityService;

    @Test
    @WithMockUser
    public void testGetAllAvailabilities() throws Exception {
        given(availabilityService.getAllAvailabilities()).willReturn(Arrays.asList(new Availability(), new Availability()));
        mockMvc.perform(get("/api/v1/availability/"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testGetAvailabilityById() throws Exception {
        UUID id = UUID.randomUUID();
        given(availabilityService.getAvailabilityById(id)).willReturn(new Availability());
        mockMvc.perform(get("/api/v1/availability/" + id))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testCreateAvailability() throws Exception {
        AvailabilityDTO availabilityDTO = new AvailabilityDTO();
        availabilityDTO.setStartDate(new Date());
        availabilityDTO.setEndDate(new Date());
        availabilityDTO.setJobCategoryId(UUID.randomUUID());
        availabilityDTO.setJobTitle("jobTitle");

        Availability availability = new Availability();
        availability.setStartDate(new Date());
        availability.setEndDate(new Date());
        availability.setJobCategoryId(UUID.randomUUID());
        availability.setJobTitle("jobTitle");

        given(availabilityService.createAvailability(any(AvailabilityDTO.class))).willReturn(availability);

        mockMvc.perform(post("/api/v1/availability/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(availabilityDTO))
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testUpdateAvailability() throws Exception {
        AvailabilityDTO availabilityDTO = new AvailabilityDTO();
        availabilityDTO.setStartDate(new Date());
        availabilityDTO.setEndDate(new Date());
        availabilityDTO.setJobCategoryId(UUID.randomUUID());
        availabilityDTO.setJobTitle("jobTitle");

        Availability availability = new Availability();
        availability.setStartDate(new Date());
        availability.setEndDate(new Date());
        availability.setJobCategoryId(UUID.randomUUID());
        availability.setJobTitle("jobTitle");

        given(availabilityService.updateAvailability(any(AvailabilityDTO.class))).willReturn(availability);

        mockMvc.perform(put("/api/v1/availability/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(availabilityDTO))
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testDeleteAvailability() throws Exception {
        UUID id = UUID.randomUUID();
        mockMvc.perform(delete("/api/v1/availability/" + id).with(csrf()))
                .andExpect(status().isOk());
    }
}