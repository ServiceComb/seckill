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

package io.servicecomb.poc.demo.seckill.event;

import io.servicecomb.poc.demo.seckill.entities.PromotionEntity;
import io.servicecomb.poc.demo.seckill.json.ToJsonFormat;

public class PromotionStartEvent<T> extends SecKillEvent {

  protected PromotionEntity promotion;

  public PromotionStartEvent() {
    super();
    this.type = PromotionStartEvent.class.getSimpleName();
  }

  public PromotionStartEvent(PromotionEntity promotion) {
    this();
    this.promotionId = promotion.getPromotionId();
    this.promotion = promotion;
  }

  @Override
  public String json(ToJsonFormat toJsonFormat) {
    return toJsonFormat.toJson(promotion);
  }
}
