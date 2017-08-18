/*
 *   Copyright 2017 Huawei Technologies Co., Ltd
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package io.servicecomb.poc.demo.seckill;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.servicecomb.poc.demo.CommandServiceApplication;
import io.servicecomb.poc.demo.seckill.dto.CouponDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommandServiceApplication.class)
@WebAppConfiguration
@AutoConfigureMockMvc
public class SecKillCommandApplicationTest {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void grabCouponUseStringCustomeIdSuccessfully() throws Exception {
    mockMvc.perform(post("/command/coupons/").contentType(APPLICATION_JSON)
        .content(toJson(new CouponDto<>("zyy"))))
        .andExpect(status().isOk()).andExpect(content().string("Request accepted"));
  }

  @Test
  public void grabCouponUseIntCustomeIdSuccessfully() throws Exception {
    mockMvc.perform(post("/command/coupons/").contentType(APPLICATION_JSON)
        .content(toJson(new CouponDto<>(10001))))
        .andExpect(status().isOk()).andExpect(content().string("Request accepted"));
  }

  @Test
  public void failsGrabCouponWhenCustomeIdIsInvalid() throws Exception {
    mockMvc.perform(post("/command/coupons/").contentType(APPLICATION_JSON)
        .content(toJson(new CouponDto<>())))
        .andExpect(status().isBadRequest()).andExpect(content().string(containsString("Invalid coupon")));
  }

  private String toJson(CouponDto value) throws JsonProcessingException {
    return objectMapper.writeValueAsString(value);
  }
}
