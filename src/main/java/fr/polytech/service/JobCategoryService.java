package fr.polytech.service;

import fr.polytech.model.JobCategoryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class JobCategoryService {

    private final Logger logger = LoggerFactory.getLogger(JobCategoryService.class);

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Get a Job Category by id.
     */
    public JobCategoryDTO getJobCategoryById(UUID id, String token) throws HttpClientErrorException {
        String uri = System.getenv("JOB_CATEGORY_API_URI") + "/" + id;
        return makeApiCall(uri, HttpMethod.GET, JobCategoryDTO.class, token);
    }

    /**
     * Make an API call.
     *
     * @param uri          URI of the API
     * @param method       HTTP method
     * @param responseType Class of the response
     * @param token        String - Access token from the user who adds the review
     * @param <T>          Type of the response
     * @return Response
     * @throws HttpClientErrorException if an error occurs while calling the API
     */
    private <T> T makeApiCall(String uri, HttpMethod method, Class<T> responseType, String token) throws HttpClientErrorException {
        logger.info("Making API call to {}", uri);
        HttpHeaders headers = createHeaders(token);
        HttpEntity<Void> entity = new HttpEntity<>(null, headers);
        ResponseEntity<T> response = restTemplate.exchange(uri, method, entity, responseType);
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new HttpClientErrorException(response.getStatusCode());
        }
    }

    /**
     * Create headers for API calls.
     *
     * @param token String - Access token from the user who adds the review
     * @return HttpHeaders
     * @throws HttpClientErrorException if the token is not valid
     */
    private HttpHeaders createHeaders(String token) throws HttpClientErrorException {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token.replace("Bearer ", ""));
        return headers;
    }

}
