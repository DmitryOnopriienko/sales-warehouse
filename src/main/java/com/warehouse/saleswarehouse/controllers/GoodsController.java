package com.warehouse.saleswarehouse.controllers;

import com.warehouse.saleswarehouse.constants.GlobalConstants;
import com.warehouse.saleswarehouse.constants.GoodsConstants;
import com.warehouse.saleswarehouse.dao.GoodsDao;
import com.warehouse.saleswarehouse.exceptions.NotFoundException;
import com.warehouse.saleswarehouse.models.Goods;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

  private final GoodsDao goodsDao;

  @Autowired
  public GoodsController(GoodsDao goodsDao) {
    this.goodsDao = goodsDao;
  }

  @GetMapping
  public String getAll(Model model) {
    model.addAttribute(GoodsConstants.GOODS_LIST_ATTR, goodsDao.getAll());
    return "goods/goods";
  }

  @GetMapping("/{id}")
  public String getById(@PathVariable("id") int id, Model model) {
    Goods goods = goodsDao.getById(id);
    if (goods == null) {
      throw new NotFoundException();
    }
    model.addAttribute(GoodsConstants.GOODS_ATTR, goods);
    return "goods/goods-info";
  }

  @GetMapping("/new")
  public String newGoods(Model model) {
    model.addAttribute(GoodsConstants.NEW_GOODS_MODEL_ATTR, new Goods());
    return "goods/goods-creating";
  }

  @PostMapping
  public String createGoods(@ModelAttribute(GoodsConstants.NEW_GOODS_MODEL_ATTR) @Valid Goods goods,
                            BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "goods/goods-creating";
    }
    goodsDao.save(goods);
    return "redirect:/goods";
  }

  @DeleteMapping("/{id}")
  public String delete(@PathVariable("id") int id) {
    goodsDao.delete(id);
    return "redirect:/goods";
  }

  @GetMapping("/{id}/edit")
  public String editProduct(@PathVariable("id") int id, Model model) {
    model.addAttribute(GoodsConstants.GOODS_ATTR, goodsDao.getById(id));
    return "goods/edit";
  }

  @PatchMapping("/{id}")
  public String update(@ModelAttribute(GoodsConstants.GOODS_ATTR) @Valid Goods goods,
                       BindingResult bindingResult, @PathVariable("id") int id) {
    if (bindingResult.hasErrors()) {
      return "goods/edit";
    }
    goodsDao.update(id, goods);
    return "redirect:/goods";
  }

  @GetMapping("/search-by-id")
  public String searchById(@RequestParam(value = "id", required = false) int id) {
    return "redirect:/goods/" + id;
  }

  @GetMapping("/search-by-text")
  public String searchByText(@RequestParam(value = "text", required = false) String text,
                             Model model) {
    if (text == null || text.isBlank()) {
      return "redirect:/goods";
    }
    List<Goods> goodsList = goodsDao.findByText(text);
    model.addAttribute(GlobalConstants.SEARCH_PATTERN, text);
    model.addAttribute(GoodsConstants.GOODS_LIST_ATTR, goodsList);
    return "goods/search-results";
  }
}
