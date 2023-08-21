package com.project.ecommerce.service;

import java.util.List;

public interface QueueInfoService {

    String getRoutingKeyByUsername(String username);

    String getQueueNameByUsername(String username);
}
