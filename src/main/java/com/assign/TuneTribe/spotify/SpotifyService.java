package com.assign.TuneTribe.spotify;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.core.ParameterizedTypeReference;
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
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<Map<String, Object>>() {
                    });
            Map<String, Object> body = response.getBody();
            if (body == null) {
                return Optional.empty();
            }
            Object tracksObj = body.get("tracks");
            if (!(tracksObj instanceof Map)) {
                return Optional.empty();
            }
            Map<?, ?> tracks = (Map<?, ?>) tracksObj;
            Object itemsObj = tracks.get("items");
            if (!(itemsObj instanceof List)) {
                return Optional.empty();
            }
            List<?> items = (List<?>) itemsObj;
            if (items.isEmpty() || !(items.get(0) instanceof Map)) {
                return Optional.empty();
            }
            Map<?, ?> first = (Map<?, ?>) items.get(0);
            Object nameObj = first.get("name");
            String name = nameObj != null ? nameObj.toString() : null;
            String trackUrl = null;
            Object externalUrlsObj = first.get("external_urls");
            if (externalUrlsObj instanceof Map) {
                Map<?, ?> externalUrls = (Map<?, ?>) externalUrlsObj;
                Object spotifyUrl = externalUrls.get("spotify");
                if (spotifyUrl != null) {
                    trackUrl = spotifyUrl.toString();
                }
            }
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
            logger.warn("Spotify credentials not configured.");
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
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    TOKEN_URL,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<Map<String, Object>>() {
                    });
            Map<String, Object> body = response.getBody();
            if (body == null) {
                return null;
            }
            Object tokenObj = body.get("access_token");
            Object expiresObj = body.get("expires_in");
            if (tokenObj == null || expiresObj == null) {
                return null;
            }
            accessToken = tokenObj.toString();
            long expiresIn = Long.parseLong(expiresObj.toString());
            accessTokenExpiresAt = Instant.now().plusSeconds(expiresIn);
            return accessToken;
        } catch (Exception ex) {
            logger.warn("Spotify token request failed", ex);
            return null;
        }
    }
}
