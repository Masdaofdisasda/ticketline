package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

public record EventSearchRequest(String artistName, String country, String city, String street, Integer zipCode, String venueName, String eventHall,
                                 @DateTimeFormat(iso = DATE_TIME) LocalDateTime startTime, @DateTimeFormat(iso = DATE_TIME) LocalDateTime endTime,
                                 String category,
                                 String nameOfEvent,
                                 int pageIndex,
                                 int pageSize) {
}