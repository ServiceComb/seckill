package io.servicecomb.poc.demo.seckill.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CouponSecKill {

  private String id;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public CouponSecKill() { }

  public CouponSecKill(String id) {
    this.id = id;
  }
}
