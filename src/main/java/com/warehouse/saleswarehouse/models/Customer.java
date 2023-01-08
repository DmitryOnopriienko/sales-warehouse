package com.warehouse.saleswarehouse.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

  private int id;

  @Length(min = 2, max = 25, message = "Name length must be between 2 and 25")
  private String name;

  @Length(min = 2, max = 25, message = "Surname length must be between 2 and 25")
  private String surname;

  @NotEmpty(message = "Email must not be empty")
  @Email(message = "Email must be valid")
  private String email;
}
