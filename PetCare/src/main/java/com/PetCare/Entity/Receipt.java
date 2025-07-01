package com.PetCare.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("receipts")
public class Receipt {
    @Id
    private Integer receiptId;
    private Integer saleId;
    private Integer appointmentId;
    private LocalDateTime receiptDate;
    private String filePath;
}
