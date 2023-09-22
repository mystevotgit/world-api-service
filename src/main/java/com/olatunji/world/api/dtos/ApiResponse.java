package com.olatunji.world.api.dtos;

import lombok.*;

import java.util.Map;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse {
    private boolean error;
    private String msg;
    private Map<String, String> errors;
}
