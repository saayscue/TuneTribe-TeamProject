package com.assign.TuneTribe.spotify;

import java.time.Instant;
import java.util.Optional;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.*;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class SpotifyService {

    public record TrackInfo(String name, String url) {
    }

    private static final Logger logger = LoggerFactory.getLogger(SpotifyService.class);
    private static final String TOKEN_URL = "https://accounts.spotify.com/api/token";
    private static final String SEARCH_URL = "https://api.spotify.com/v1/search";
    private final RestTemplate restTemplate;
    private final SpotifyProperties spotifyProperties;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private String accessToken;
    private Instant accessTokenExpiresAt;

    public SpotifyService(RestTemplateBuilder restTemplateBuilder, SpotifyProperties spotifyProperties) {
        this.restTemplate = restTemplateBuilder.build();
        this.spotifyProperties = spotifyProperties;
    }

    public Optional<TrackInfo> searchTrack(String title, String artist) {
        String token = getAccessToken();
        if (token == null || token.isBlank()) {
            return Optional.empty();
        }
        if (title == null || title.isBlank()) {
            return Optional.empty();
        }

        String query = buildQuery(title, artist);
        String url = UriComponentsBuilder.fromHttpUrl(SEARCH_URL)
                .queryParam("q", query)
                .queryParam("type", "track")
                .queryParam("limit", 1)
                .build()
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                return Optional.empty();
            }

            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode firstItem = root.path("tracks").path("items").path(0);
            String name = firstItem.path("name").asText(null);
            String trackUrl = firstItem.path("external_urls").path("spotify").asText(null);

            if (name == null || name.isBlank()) {
                return Optional.empty();
            }
            return Optional.of(new TrackInfo(name, trackUrl));
        } catch (Exception ex) {
            logger.warn("Spotify search failed", ex);
            return Optional.empty();
        }
    }

    private String buildQuery(String title, String artist) {
        StringBuilder builder = new StringBuilder();
        builder.append("track:").append(title.trim());
        if (artist != null && !artist.isBlank()) {
            builder.append(" artist:").append(artist.trim());
        }
        return builder.toString();
    }

    private synchronized String getAccessToken() {
        String clientId = spotifyProperties.getClientId();
        String clientSecret = spotifyProperties.getClientSecret();
        if (clientId == null || clientId.isBlank() || clientSecret == null || clientSecret.isBlank()) {
            logger.warn("No spotify credentials.");
            return null;
        }
        if (accessToken != null && accessTokenExpiresAt != null
                && Instant.now().isBefore(accessTokenExpiresAt.minusSeconds(30))) {
            return accessToken;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(clientId, clientSecret);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(form, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    TOKEN_URL,
                    HttpMethod.POST,
                    entity,
                    String.class);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                return null;
            }

            JsonNode root = objectMapper.readTree(response.getBody());
            String token = root.path("access_token").asText(null);
            long expiresIn = root.path("expires_in").asLong(0);

            if (token == null || expiresIn <= 0) {
                return null;
            }

            accessToken = token;
            accessTokenExpiresAt = Instant.now().plusSeconds(expiresIn);
            return accessToken;
        } catch (Exception ex) {
            logger.warn("Spotify token failed", ex);
            return null;
        }
    }
}