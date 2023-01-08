package com.warehouse.saleswarehouse.dao;

import com.warehouse.saleswarehouse.models.Customer;
import com.warehouse.saleswarehouse.models.mappers.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomersDao {

  private final JdbcTemplate jdbcTemplate;

  private final CustomerMapper customerMapper;

  @Autowired
  public CustomersDao(JdbcTemplate jdbcTemplate, CustomerMapper customerMapper) {
    this.jdbcTemplate = jdbcTemplate;
    this.customerMapper = customerMapper;
  }

  public List<Customer> getAll() {
    return jdbcTemplate.query("SELECT * FROM consumer", customerMapper);
  }

  public Customer getById(int id) {
    return jdbcTemplate.query("SELECT * FROM consumer WHERE id=?", customerMapper, id)
            .stream()
            .findAny()
            .orElse(null);
  }

  public void save(Customer customer) {
    jdbcTemplate.update("INSERT INTO consumer(c_name, c_surname, email) VALUES (?, ?, ?)",
            customer.getName(), customer.getSurname(), customer.getEmail());
  }

  public void delete(int id) {
    jdbcTemplate.update("DELETE FROM consumer WHERE id=?", id);
  }

  public void update(int id, Customer customer) {
    jdbcTemplate.update("UPDATE consumer SET c_name=?, c_surname=?, email=? WHERE id=?",
            customer.getName(), customer.getSurname(), customer.getEmail(), id);
  }

  public List<Customer> findByText(String text) {
    text = "%" + text + "%";
    return jdbcTemplate.query("SELECT * FROM consumer WHERE c_name LIKE ? OR c_surname LIKE ? OR email LIKE ?",
            customerMapper, text, text, text);
  }
}
