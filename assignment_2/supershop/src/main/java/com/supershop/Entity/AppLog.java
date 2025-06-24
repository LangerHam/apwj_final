package com.supershop.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppLog {
    private Long id;
    private LocalDateTime logTimestamp;
    private String username;
    private String accessedMethod;
}

