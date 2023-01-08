package com.warehouse.saleswarehouse.models.mappers;

import com.warehouse.saleswarehouse.dao.CustomersDao;
import com.warehouse.saleswarehouse.models.Waybill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class WaybillMapper implements RowMapper<Waybill> {

  private final CustomersDao customersDao;

  @Autowired
  public WaybillMapper(CustomersDao customersDao) {
    this.customersDao = customersDao;
  }

  @Override
  public Waybill mapRow(ResultSet rs, int rowNum) throws SQLException {
    Waybill waybill = new Waybill();
    waybill.setId(rs.getInt("id"));

    int customerId = rs.getInt("consumer_id");

    waybill.setCustomerId(customerId);
    waybill.setCustomer(customersDao.getById(customerId));
    waybill.setCarNumber(rs.getString("car_number"));
    waybill.setDriverName(rs.getString("driver_name"));
    waybill.setDriverSurname(rs.getString("driver_surname"));
    waybill.setDriverPatronymic(rs.getString("driver_patronymic"));
    waybill.setServicePrice(rs.getDouble("service_price"));
    waybill.setOrderDate(rs.getDate("order_date"));

    return waybill;
  }
}
