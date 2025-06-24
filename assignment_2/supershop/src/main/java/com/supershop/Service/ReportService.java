package com.supershop.Service;

import com.supershop.Entity.Category;
import com.supershop.Entity.Order;
import com.supershop.Entity.Product;
import com.supershop.Entity.OrderItem;
import com.supershop.Repository.CategoryRepository;
import com.supershop.Repository.OrderRepository;
import com.supershop.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ReportService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OrderRepository orderRepository;
    private final ProductService productService;

    @Autowired
    public ReportService(ProductRepository productRepository, CategoryRepository categoryRepository, OrderRepository orderRepository, ProductService productService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.orderRepository = orderRepository;
        this.productService = productService;
    }

    public Map<String, BigDecimal> generateCategoryValueReport() {
        List<Product> allProducts = productRepository.findAll();
        List<Category> allCategories = categoryRepository.findAll();
        Map<Long, String> categoryIdToNameMap = allCategories.stream()
                .collect(Collectors.toMap(Category::getId, Category::getName));

        return allProducts.stream()
                .filter(Product::isAvailable)
                .collect(Collectors.groupingBy(
                        product -> categoryIdToNameMap.get(product.getCategoryId()),
                        Collectors.mapping(
                                product -> product.getPrice().multiply(new BigDecimal(product.getQuantity())),
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ));
    }
    public List<Product> generateDiscountedProductsReport() {
        return productService.getProductsNearingExpiry();
    }
    public Map<String, Object> generateMonthlySalesReport(int year, int month) {
        List<Order> allOrders = orderRepository.findAll();
        Month targetMonth = Month.of(month);

        List<Order> filteredOrders = allOrders.stream()
                .filter(order -> order.getOrderDate().getYear() == year && order.getOrderDate().getMonth() == targetMonth)
                .collect(Collectors.toList());
        BigDecimal totalRevenue = filteredOrders.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Map<Long, Integer> productSalesCount = new HashMap<>();
        for (Order order : filteredOrders) {
            for (OrderItem item : order.getItems()) {
                productSalesCount.merge(item.getProductId(), item.getQuantity(), Integer::sum);
            }
        }
        Map<Long, Product> productMap = productRepository.findAll().stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
        Map<Long, String> categoryMap = categoryRepository.findAll().stream()
                .collect(Collectors.toMap(Category::getId, Category::getName));
        Map<String, BigDecimal> salesPerCategory = filteredOrders.stream()
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.groupingBy(
                        orderItem -> {
                            Product product = productMap.get(orderItem.getProductId());
                            return categoryMap.getOrDefault(product.getCategoryId(), "Unknown Category");
                        },
                        Collectors.mapping(
                                orderItem -> {
                                    BigDecimal price = orderItem.getPricePerUnit();
                                    BigDecimal discount = orderItem.getDiscountApplied();
                                    BigDecimal quantity = new BigDecimal(orderItem.getQuantity());
                                    BigDecimal finalPrice = price.subtract(price.multiply(discount));
                                    return finalPrice.multiply(quantity);
                                },
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ));
        List<String> bestSellingProducts = productSalesCount.entrySet().stream()
                .sorted(Map.Entry.<Long, Integer>comparingByValue().reversed())
                .limit(5)
                .map(entry -> productRepository.findById(entry.getKey()).map(Product::getName).orElse("Unknown Product"))
                .collect(Collectors.toList());

        Map<String, Object> report = new HashMap<>();
        report.put("year", year);
        report.put("month", targetMonth.toString());
        report.put("totalOrders", filteredOrders.size());
        report.put("totalRevenue", totalRevenue);
        report.put("bestSellingProducts", bestSellingProducts);

        return report;
    }
}
