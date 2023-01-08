package com.warehouse.saleswarehouse.models.mappers;

import com.warehouse.saleswarehouse.constants.GoodsConstants;
import com.warehouse.saleswarehouse.models.Goods;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class GoodsMapper implements RowMapper<Goods> {
  @Override
  public Goods mapRow(ResultSet rs, int rowNum) throws SQLException {
    Goods goods = new Goods();

    goods.setId(rs.getInt(GoodsConstants.GOODS_ID_ROW_NAME));
    goods.setName(rs.getString(GoodsConstants.GOODS_NAME_ROW_NAME));
    goods.setPrice(rs.getDouble(GoodsConstants.GOODS_PRICE_ROW_NAME));
    goods.setComment(rs.getString(GoodsConstants.GOODS_COMMENT_ROW_NAME));

    return goods;
  }
}
