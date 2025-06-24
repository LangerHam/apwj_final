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
public class WishlistItem {
    private Long id;
    private Long userId;
    private Long productId;
    private LocalDateTime addedDate;
}
