package com.oceanstyxx.pubdriver.model;

/**
 * Created by mohsin on 10/10/16.
 */

public class Pub {

    private Integer id;

    private String pub_name;

    private String address;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPub_name() {
        return pub_name;
    }

    public void setPub_name(String pub_name) {
        this.pub_name = pub_name;
    }

}
