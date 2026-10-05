package com.works;
import java.time.Instant;

public record OrderEvent(String orderId, String status, Instant occurredAt) {}
