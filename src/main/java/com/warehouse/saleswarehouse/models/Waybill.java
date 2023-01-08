package com.warehouse.saleswarehouse.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Waybill {

  private int id;

  private Customer customer;

  private int customerId;

  private double servicePrice;

  private String carNumber;

  private String driverName;

  private String driverSurname;

  private Date orderDate;

  private String driverPatronymic;

  private Map<Goods, Long> goods;

  private double totalPrice;
}
