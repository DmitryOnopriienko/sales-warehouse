package com.warehouse.saleswarehouse.controllers;

import com.warehouse.saleswarehouse.dao.WaybillsDao;
import com.warehouse.saleswarehouse.exceptions.NotFoundException;
import com.warehouse.saleswarehouse.models.Goods;
import com.warehouse.saleswarehouse.models.Waybill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/waybills")
public class WaybillsController {

  private final WaybillsDao waybillsDao;

  @Autowired
  public WaybillsController(WaybillsDao waybillsDao) {
    this.waybillsDao = waybillsDao;
  }

  @GetMapping
  public String getAll(
          @RequestParam(value = "order-by", required = false, defaultValue = "id") String orderBy,
          Model model) {
    List<Waybill> waybills;
    switch (orderBy) {
      case "id" -> waybills = waybillsDao.getAllWithListOfGoods();
      case "service_price" -> {
        waybills = waybillsDao.getAllWithListOfGoods();
        waybills.sort(Comparator.comparingDouble(Waybill::getServicePrice));
      }
      case "total_price" -> {
        waybills = waybillsDao.getAllWithListOfGoods();
        waybills.sort(Comparator.comparingDouble(Waybill::getTotalPrice));
      }
      case "order_date" -> {
        waybills = waybillsDao.getAllWithListOfGoods();
        waybills.sort(Comparator.comparing(Waybill::getOrderDate));
      }
      default -> {
        orderBy = "id";
        waybills = waybillsDao.getAllWithListOfGoods();
      }
    }
    model.addAttribute("orderBy", orderBy);
    model.addAttribute("waybills", waybills);
    return "waybills/waybills";
  }

  @GetMapping("/{id}")
  public String getById(@PathVariable("id") int id, Model model) {
    Waybill waybill = waybillsDao.getById(id);
    if (waybill == null) {
      throw new NotFoundException();
    }
    model.addAttribute("waybill", waybill);
    Map<Goods, Long> goodsOfWaybill = waybillsDao.getGoodsForWaybill(id);
    model.addAttribute("goodsMap", goodsOfWaybill);
    model.addAttribute("totalPrice", waybillsDao.getTotalPriceOfWaybill(goodsOfWaybill));

    return "waybills/waybill-info";
  }
}
