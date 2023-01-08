package com.warehouse.saleswarehouse.controllers;

import com.warehouse.saleswarehouse.constants.CustomersConstants;
import com.warehouse.saleswarehouse.constants.GlobalConstants;
import com.warehouse.saleswarehouse.dao.CustomersDao;
import com.warehouse.saleswarehouse.dao.WaybillsDao;
import com.warehouse.saleswarehouse.exceptions.NotFoundException;
import com.warehouse.saleswarehouse.models.Customer;
import com.warehouse.saleswarehouse.models.Waybill;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/customers")
public class CustomersController {

  private final CustomersDao customersDao;

  private final WaybillsDao waybillsDao;

  @Autowired
  public CustomersController(CustomersDao customersDao, WaybillsDao waybillsDao) {
    this.customersDao = customersDao;
    this.waybillsDao = waybillsDao;
  }

  @GetMapping
  public String getAll(Model model) {
    model.addAttribute(CustomersConstants.CUSTOMERS_LIST_ATTR, customersDao.getAll());
    return "customers/customers";
  }

  @GetMapping("/{id}")
  public String getById(@PathVariable("id") int id, Model model) {
    Customer customer = customersDao.getById(id);
    if (customer == null) {
      throw new NotFoundException();
    }
    model.addAttribute(CustomersConstants.CUSTOMER_ATTR, customer);
    return "customers/customer-info";
  }

  @GetMapping("/new")
  public String newCustomer(Model model) {
    model.addAttribute(CustomersConstants.CUSTOMER_ATTR, new Customer());
    return "customers/customer-creating";
  }

  @PostMapping
  public String createCustomer(@ModelAttribute(CustomersConstants.CUSTOMER_ATTR) @Valid Customer customer,
                            BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "customers/customer-creating";
    }
    customersDao.save(customer);
    return "redirect:/customers";
  }

  @DeleteMapping("/{id}")
  public String delete(@PathVariable("id") int id) {
    customersDao.delete(id);
    return "redirect:/customers";
  }

  @GetMapping("/{id}/edit")
  public String editCustomer(@PathVariable("id") int id, Model model) {
    model.addAttribute(CustomersConstants.CUSTOMER_ATTR, customersDao.getById(id));
    return "customers/edit";
  }

  @PatchMapping("/{id}")
  public String update(@ModelAttribute(CustomersConstants.CUSTOMER_ATTR) @Valid Customer customer,
                       BindingResult bindingResult, @PathVariable("id") int id) {
    if (bindingResult.hasErrors()) {
      return "customers/edit";
    }
    customersDao.update(id, customer);
    return "redirect:/goods";
  }

  @GetMapping("/search-by-id")
  public String searchById(@RequestParam("id") int id) {
    return "redirect:/customers/" + id;
  }

  @GetMapping("/search-by-text")
  public String searchByText(@RequestParam(value = "text", required = false) String text,
                             Model model) {
    if (text == null || text.isBlank()) {
      return "redirect:/customers";
    }
    List<Customer> customers = customersDao.findByText(text);
    model.addAttribute(GlobalConstants.SEARCH_PATTERN, text);
    model.addAttribute(CustomersConstants.CUSTOMERS_LIST_ATTR, customers);
    return "customers/search-results";
  }

  @GetMapping("/{id}/waybills")
  public String waybills(@PathVariable("id") int id,
                         @RequestParam(value = "order-by", required = false, defaultValue = "id") String orderBy,
                         Model model) {
    Customer customer = customersDao.getById(id);
    List<Waybill> waybills;
    switch (orderBy) {
      case "id" -> waybills = waybillsDao.getWaybillsOfCustomerOrderById(id);
      case "service_price" -> waybills = waybillsDao.getWaybillsOfCustomerOrderByServicePrice(id);
      case "total_price" -> waybills = waybillsDao.getWaybillsOfCustomerOrderByTotalPrice(id);
      case "order_date" -> waybills = waybillsDao.getWaybillsOfCustomerOrderByDate(id);
      default -> {
        orderBy = "id";
        waybills = waybillsDao.getWaybillsOfCustomerOrderById(id);
      }
    }
    model.addAttribute("customer", customer);
    model.addAttribute("orderBy", orderBy);
    model.addAttribute("customerWaybills", waybills);
    return "customers/waybills-of-customer";
  }
}
