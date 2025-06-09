package com.supershop.shop.Api;

import com.supershop.shop.Entity.Products;
import com.supershop.shop.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.Collections;

@Controller
public class ProductApi {
    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView home() {
        return new ModelAndView("redirect:/products");
    }

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public ModelAndView showProductsPage() {
        ModelAndView mav = new ModelAndView("products");
        mav.addObject("expiringProducts", Collections.emptyList());
        mav.addObject("discountedProducts", Collections.emptyList());
        mav.addObject("categoryTotals", Collections.emptyList());
        return mav;
    }

    @RequestMapping(value = "/products/expiring", method = RequestMethod.GET)
    public ModelAndView getExpiringProducts() {
        ModelAndView mav = new ModelAndView("products");
        mav.addObject("expiringProducts", productService.getExpiringProducts());
        mav.addObject("discountedProducts", Collections.emptyList());
        mav.addObject("categoryTotals", Collections.emptyList());
        return mav;
    }

    @RequestMapping(value = "/products/expiring/discounted", method = RequestMethod.POST)
    public ModelAndView getExpiringProductsWithDiscount(@RequestParam BigDecimal discountPercentage) {
        ModelAndView mav = new ModelAndView("products");
        mav.addObject("expiringProducts", Collections.emptyList());
        mav.addObject("discountedProducts", productService.getExpiringProductsWithDiscount(discountPercentage));
        mav.addObject("categoryTotals", Collections.emptyList());
        return mav;
    }

    @RequestMapping(value = "/products/total-price", method = RequestMethod.GET)
    public ModelAndView getTotalPriceByCategory() {
        ModelAndView mav = new ModelAndView("products");
        mav.addObject("expiringProducts", Collections.emptyList());
        mav.addObject("discountedProducts", Collections.emptyList());
        mav.addObject("categoryTotals", productService.getTotalPriceByCategory());
        return mav;
    }

    @RequestMapping(value = "/products/add", method = RequestMethod.POST)
    public String addProduct(@ModelAttribute Products product) {
        productService.addProduct(product);
        return "redirect:/products";
    }

    @RequestMapping(value = "/products/modify", method = RequestMethod.POST)
    public String modifyProduct(@ModelAttribute Products product) {
        productService.editProduct(product);
        return "redirect:/products";
    }
}
