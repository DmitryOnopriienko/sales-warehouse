package com.warehouse.saleswarehouse.dao;

import com.warehouse.saleswarehouse.models.Goods;
import com.warehouse.saleswarehouse.models.mappers.GoodsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsDao {

  private final JdbcTemplate jdbcTemplate;

  private final GoodsMapper goodsMapper;

  @Autowired
  public GoodsDao(JdbcTemplate jdbcTemplate, GoodsMapper goodsMapper) {
    this.jdbcTemplate = jdbcTemplate;
    this.goodsMapper = goodsMapper;
  }

  public List<Goods> getAll() {
    return jdbcTemplate.query("SELECT * FROM goods", goodsMapper);
  }

  public Goods getById(int id) {
    return jdbcTemplate.query("SELECT * FROM goods WHERE id=?", goodsMapper, id)
            .stream()
            .findAny()
            .orElse(null);
  }

  public void save(Goods goods) {
    String comment = goods.getComment();
    if (comment == null || comment.isBlank()) {   // case if comment is not provided
      jdbcTemplate.update("INSERT INTO goods(g_name, price) VALUES (?, ?)",
              goods.getName(), goods.getPrice());
      return;
    }
    jdbcTemplate.update("INSERT INTO goods(g_name, price, comment) VALUES (?, ?, ?)",
            goods.getName(), goods.getPrice(), comment);
  }

  public void delete(int id) {
    jdbcTemplate.update("DELETE FROM goods WHERE id=?", id);
  }

  public void update(int id, Goods goods) {
    jdbcTemplate.update("UPDATE goods SET g_name=?, price=?, comment=? WHERE id=?",
            goods.getName(), goods.getPrice(), goods.getComment(), id);
  }

  public List<Goods> findByText(String text) {
    text = "%" + text + "%";
    return jdbcTemplate.query("SELECT * FROM goods WHERE g_name LIKE ? OR comment LIKE ?",
            goodsMapper, text, text);
  }
}
