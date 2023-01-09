package com.warehouse.saleswarehouse.dao;

import com.warehouse.saleswarehouse.models.Goods;
import com.warehouse.saleswarehouse.models.Waybill;
import com.warehouse.saleswarehouse.models.mappers.WaybillMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class WaybillsDao {

  private final JdbcTemplate jdbcTemplate;

  private final WaybillMapper waybillMapper;

  private final GoodsDao goodsDao;

  @Autowired
  public WaybillsDao(JdbcTemplate jdbcTemplate, WaybillMapper waybillMapper, GoodsDao goodsDao) {
    this.jdbcTemplate = jdbcTemplate;
    this.waybillMapper = waybillMapper;
    this.goodsDao = goodsDao;
  }

  public List<Waybill> getAll() {
    return jdbcTemplate.query("SELECT * FROM waybill", waybillMapper);
  }

  public List<Waybill> getAllWithListOfGoods() {
    List<Waybill> waybills = getAll();
    waybills.forEach(this::setGoodsAndTotalPrice);
    return waybills;
  }

  private void setGoodsAndTotalPrice(Waybill waybill) {
    waybill.setGoods(getGoodsForWaybill(waybill.getId()));
    waybill.setTotalPrice(getTotalPriceOfWaybill(waybill.getId()));
  }

  public Waybill getById(int id) {
    return jdbcTemplate.query("SELECT * FROM waybill WHERE id=?", waybillMapper, id)
            .stream()
            .findAny()
            .orElse(null);
  }

  public Map<Goods, Long> getGoodsForWaybill(int waybillId) {
    String sql = """
            SELECT goods_id, amount
            FROM waybill_has_goods
            WHERE waybill_id=?
            ORDER BY goods_id
            """;
    Map<Goods, Long> goodsMap = new LinkedHashMap<>();
    for (Map<String, Object> row : jdbcTemplate.queryForList(sql, waybillId)) {
      int goods_id = (int) row.get("goods_id");
      long amount = (long) row.get("amount"); // long bcs in table 'amount' is unsigned int
      Goods product = goodsDao.getById(goods_id);
      if (product == null) {
        continue;
      }
      goodsMap.put(product, amount);
    }
    return goodsMap;
  }

  public double getTotalPriceOfWaybill(int id) {
    Map<Goods, Long> productsAndAmount = getGoodsForWaybill(id);
    double totalPrice = 0;
    for (var product : productsAndAmount.entrySet()) {
      totalPrice += product.getKey().getPrice() * product.getValue();
    }
    return Math.round(totalPrice * 100.0) / 100.0;
  }

  public double getTotalPriceOfWaybill(Map<Goods, Long> productsAndAmount) {
    double totalPrice = 0;
    for (var product : productsAndAmount.entrySet()) {
      totalPrice += product.getKey().getPrice() * product.getValue();
    }
    return Math.round(totalPrice * 100.0) / 100.0;
  }

  public List<Waybill> getWaybillsOfCustomerOrderById(int id) {
    String sql = """
            SELECT * FROM waybill
            WHERE consumer_id=?
            """;
    List<Waybill> waybills = jdbcTemplate.query(sql, waybillMapper, id);
    waybills.forEach(this::setGoodsAndTotalPrice);
    return waybills;
  }

  public List<Waybill> getWaybillsOfCustomerOrderByServicePrice(int id) {
    String sql = """
            SELECT * FROM waybill
            WHERE consumer_id=?
            ORDER BY service_price
            """;
    List<Waybill> waybills = jdbcTemplate.query(sql, waybillMapper, id);
    waybills.forEach(this::setGoodsAndTotalPrice);
    return waybills;
  }

  public List<Waybill> getWaybillsOfCustomerOrderByTotalPrice(int id) {
    String sql = """
            SELECT * FROM waybill
            WHERE consumer_id=?
            """;
    List<Waybill> waybills = jdbcTemplate.query(sql, waybillMapper, id);
    waybills.forEach(this::setGoodsAndTotalPrice);
    waybills.sort(Comparator.comparingDouble(Waybill::getTotalPrice));
    return waybills;
  }

  public List<Waybill> getWaybillsOfCustomerOrderByDate(int id) {
    String sql = """
            SELECT * FROM waybill
            WHERE consumer_id=?
            ORDER BY order_date
            """;
    List<Waybill> waybills = jdbcTemplate.query(sql, waybillMapper, id);
    waybills.forEach(this::setGoodsAndTotalPrice);
    return waybills;
  }

  public void delete(int id) {
    jdbcTemplate.update("DELETE FROM waybill WHERE id=?", id);
  }
}
