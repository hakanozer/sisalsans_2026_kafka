package com.order;

import org.springframework.stereotype.Service;
import com.customer.CustomerModel;


@Service
public class OrderService {

    public void placeOrder() {
        CustomerModel customer = new CustomerModel("John", "", "1234567890", 100);
    }

}
