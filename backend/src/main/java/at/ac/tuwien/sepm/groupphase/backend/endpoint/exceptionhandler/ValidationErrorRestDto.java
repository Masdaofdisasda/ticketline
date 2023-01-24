package at.ac.tuwien.sepm.groupphase.backend.endpoint.exceptionhandler;

import java.util.List;

public record ValidationErrorRestDto(String message, List<String> errors) {}
