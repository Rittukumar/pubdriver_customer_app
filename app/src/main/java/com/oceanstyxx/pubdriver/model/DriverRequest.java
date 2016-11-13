package com.oceanstyxx.pubdriver.model;

import android.view.View;

import static com.oceanstyxx.pubdriver.R.id.pickupaddress;
import static com.oceanstyxx.pubdriver.R.id.pickupvenue;

/**
 * Created by mohsin on 10/10/16.
 */

public class DriverRequest {

    private Integer id;

    private String driverCode;

    private Integer pubId;

    private Integer termsAndConditions;

    private Integer transmissionId;

    private Integer carTypeId;

    private String pickupvenue;

    private String pickupaddress;

    private String orderSrc;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDriverCode() {
        return driverCode;
    }

    public void setDriverCode(String driverCode) {
        this.driverCode = driverCode;
    }

    public Integer getPubId() {
        return pubId;
    }

    public void setPubId(Integer pubId) {
        this.pubId = pubId;
    }

    public Integer getTermsAndConditions() {
        return termsAndConditions;
    }

    public void setTermsAndConditions(Integer termsAndConditions) {
        this.termsAndConditions = termsAndConditions;
    }

    public Integer getTransmissionId() {
        return transmissionId;
    }

    public void setTransmissionId(Integer transmissionId) {
        this.transmissionId = transmissionId;
    }

    public Integer getCarTypeId() {
        return carTypeId;
    }

    public void setCarTypeId(Integer carTypeId) {
        this.carTypeId = carTypeId;
    }

    public String getPickupvenue() {
        return pickupvenue;
    }

    public void setPickupvenue(String pickupvenue) {
        this.pickupvenue = pickupvenue;
    }

    public String getPickupaddress() {
        return pickupaddress;
    }

    public void setPickupaddress(String pickupaddress) {
        this.pickupaddress = pickupaddress;
    }

    public String getOrderSrc() {
        return orderSrc;
    }

    public void setOrderSrc(String orderSrc) {
        this.orderSrc = orderSrc;
    }
}
