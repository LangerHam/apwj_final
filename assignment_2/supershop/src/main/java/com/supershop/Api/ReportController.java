package com.supershop.Api;

import com.supershop.Entity.Product;
import com.supershop.Service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/reports")
@PreAuthorize("hasRole('ADMIN')")
public class ReportController {
    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }
    @GetMapping("/category-value")
    public ResponseEntity<Map<String, BigDecimal>> getCategoryValueReport() {
        return ResponseEntity.ok(reportService.generateCategoryValueReport());
    }
    @GetMapping("/discounted-products")
    public ResponseEntity<List<Product>> getDiscountedProductsReport() {
        return ResponseEntity.ok(reportService.generateDiscountedProductsReport());
    }
    @GetMapping("/monthly-sales")
    public ResponseEntity<Map<String, Object>> getMonthlySalesReport(@RequestParam int year, @RequestParam int month) {
        return ResponseEntity.ok(reportService.generateMonthlySalesReport(year, month));
    }
}
