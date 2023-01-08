package com.warehouse.saleswarehouse.models.mappers;

import com.warehouse.saleswarehouse.constants.CustomersConstants;
import com.warehouse.saleswarehouse.models.Customer;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CustomerMapper implements RowMapper<Customer> {
  @Override
  public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
    Customer customer = new Customer();

    customer.setId(rs.getInt(CustomersConstants.CUSTOMER_ID_ROW_NAME));
    customer.setName(rs.getString(CustomersConstants.CUSTOMER_NAME_ROW_NAME));
    customer.setSurname(rs.getString(CustomersConstants.CUSTOMER_SURNAME_ROW_NAME));
    customer.setEmail(rs.getString(CustomersConstants.CUSTOMER_EMAIL_ROW_NAME));

    return customer;
  }
}
