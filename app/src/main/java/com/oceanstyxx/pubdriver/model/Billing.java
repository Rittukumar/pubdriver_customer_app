package com.oceanstyxx.pubdriver.model;

/**
 * Created by mohsin on 27/10/16.
 */

public class Billing {

    private Integer id;

    private Integer drive_request_id;

    private Integer quantity;

    private Integer unit_price;

    private Integer total_price;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDrive_request_id() {
        return drive_request_id;
    }

    public void setDrive_request_id(Integer drive_request_id) {
        this.drive_request_id = drive_request_id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(Integer unit_price) {
        this.unit_price = unit_price;
    }

    public Integer getTotal_price() {
        return total_price;
    }

    public void setTotal_price(Integer total_price) {
        this.total_price = total_price;
    }
}
