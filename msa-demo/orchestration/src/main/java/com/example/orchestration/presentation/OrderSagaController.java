package com.example.orchestration.presentation;

import com.example.orchestration.application.OrderSagaService;
import com.example.orchestration.application.dto.request.OrderRequestDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orchestration/orders")
public class OrderSagaController {
    private final OrderSagaService sagaService;

    public OrderSagaController(OrderSagaService sagaService) { this.sagaService = sagaService; }

    @PostMapping
    public String startSaga(@RequestBody OrderRequestDto orderRequestDto) {
        sagaService.startSaga(orderRequestDto);
        return "주문이 접수되었습니다.";
    }
}
