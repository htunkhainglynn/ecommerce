package com.project.ecommerce.service.implementation;

import com.project.ecommerce.repo.QueueInfoRepository;
import com.project.ecommerce.repo.UserRepository;
import com.project.ecommerce.service.QueueInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QueueInfoServiceImpl implements QueueInfoService {

    @Autowired
    QueueInfoRepository queueInfoRepository;

    @Override
    public String getRoutingKeyByUsername(String username) {
        return queueInfoRepository.findRoutingKeyByUsername(username).orElseThrow(() -> new RuntimeException("User not found."));
    }

}
