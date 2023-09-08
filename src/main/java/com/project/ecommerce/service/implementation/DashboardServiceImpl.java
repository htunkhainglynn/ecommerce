package com.project.ecommerce.service.implementation;

import com.project.ecommerce.repo.OrderRepository;
import com.project.ecommerce.repo.ProductRepository;
import com.project.ecommerce.repo.ProductVariantRepository;
import com.project.ecommerce.repo.UserRepository;
import com.project.ecommerce.service.DashboardService;
import com.project.ecommerce.vo.DashBoardSummaryVo;
import com.project.ecommerce.vo.GraphDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;

    private final ProductVariantRepository productVariantRepository;

    private final OrderRepository orderRepository;

    @Autowired
    public DashboardServiceImpl(UserRepository userRepository,
                                ProductVariantRepository productVariantRepository,
                                OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.productVariantRepository = productVariantRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public DashBoardSummaryVo getSummary() {

        int totalClients = userRepository.findTotalClients();

        int totalProducts = productVariantRepository.findTotalProducts();

        int totalOrders = orderRepository.findTotalOrders();

        double totalSales = orderRepository.findTotalSales();

        return DashBoardSummaryVo.builder()
                .totalClients(totalClients)
                .totalProducts(totalProducts)
                .totalOrders(totalOrders)
                .totalSales(totalSales)
                .build();
    }

    @Override
    public List<GraphDataVo> getGraphData() {
        return null;
    }
}
