package com.project.ecommerce.service.implementation;

import com.project.ecommerce.repo.QueueInfoRepository;
import com.project.ecommerce.service.QueueInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class QueueInfoServiceImpl implements QueueInfoService {

    private final QueueInfoRepository queueInfoRepository;

    @Autowired
    public QueueInfoServiceImpl(QueueInfoRepository queueInfoRepository) {
        this.queueInfoRepository = queueInfoRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public String getRoutingKeyByUsername(String username) {
        return queueInfoRepository.findRoutingKeyByUsername(username).orElseThrow(() -> new RuntimeException("Queue not found."));
    }

    @Transactional(readOnly = true)
    @Override
    public String getQueueNameByUsername(String username) {
        return queueInfoRepository.findQueueNameByUsername(username);
    }


}
