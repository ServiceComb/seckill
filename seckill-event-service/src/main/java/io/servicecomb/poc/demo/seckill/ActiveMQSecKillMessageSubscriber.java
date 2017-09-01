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

import io.servicecomb.poc.demo.seckill.dto.EventMessageDto;
import io.servicecomb.poc.demo.seckill.event.SecKillEventFormat;
import java.util.concurrent.BlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;

public class ActiveMQSecKillMessageSubscriber implements SecKillMessageSubscriber {

  private static final Logger logger = LoggerFactory.getLogger(ActiveMQSecKillMessageSubscriber.class);
  private final BlockingQueue<EventMessageDto> messages;
  private final SecKillEventFormat eventFormat;

  public ActiveMQSecKillMessageSubscriber(BlockingQueue<EventMessageDto> messages, SecKillEventFormat eventFormat) {
    this.messages = messages;
    this.eventFormat = eventFormat;
  }

  @Override
  @JmsListener(destination = "seckill", containerFactory = "containerFactory")
  public void subscribeMessage(String messageContent) {
    logger.info("receive message : {}", messageContent);
    messages.add(eventFormat.getFormat().deserialize(messageContent, EventMessageDto.class));
  }
}
