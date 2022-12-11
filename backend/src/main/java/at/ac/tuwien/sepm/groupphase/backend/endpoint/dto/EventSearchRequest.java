package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.time.LocalDate;

public record EventSearchRequest(String artistName, String country, String city, String street, Integer zipCode, String venueName, String eventHall,
                                 String from, LocalDate startTime, String genre, String nameOfEvent, int pageIndex, int pageSize) {
}
