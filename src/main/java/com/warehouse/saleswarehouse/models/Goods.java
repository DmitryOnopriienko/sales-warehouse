package com.warehouse.saleswarehouse.models;

import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Goods {

  private int id;

  @Length(min = 1, max = 50, message = "goods name length must be between 1 and 50")
  private String name;

  @DecimalMin(value = "0.01", message = "price must be more than 0.01")
  private double price;

  private String comment;
}
