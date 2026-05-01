package com.medbill.backend.models;

public enum BillStatus {
    PENDING, COMPLETED, CANCELLED,
    PENDING_QUOTE, QUOTE_SENT, USER_CONFIRMED, NEW_ORDER, ACCEPTED
}
