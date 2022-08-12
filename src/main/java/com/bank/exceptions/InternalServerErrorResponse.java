package com.bank.exceptions;

public record InternalServerErrorResponse(int status, String message, String timestamp) {

}
