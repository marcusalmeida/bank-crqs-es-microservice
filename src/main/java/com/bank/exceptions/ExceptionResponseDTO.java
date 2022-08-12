package com.bank.exceptions;

import java.time.LocalDateTime;

public record ExceptionResponseDTO(int status, String message, LocalDateTime timestamp) {

}
