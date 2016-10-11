package com.oceanstyxx.pubdriver.model;

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
}
