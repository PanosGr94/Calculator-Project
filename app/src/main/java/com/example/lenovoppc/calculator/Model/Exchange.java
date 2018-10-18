package com.example.lenovoppc.calculator.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*
public class Exchange{

    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("base")
    @Expose
    private String base;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }
}
*/


public class Exchange {

    @SerializedName("USD")
    @Expose
    private double ex_usd;
    @SerializedName("MXN")
    @Expose
    private double ex_mex;
    @SerializedName("JPY")
    @Expose
    private double ex_jap;
    @SerializedName("GBP")
    @Expose
    private double ex_gbp;
    @SerializedName("AUD")
    @Expose
    private double ex_aus;

    public double getEx_usd() {
        return ex_usd;
    }

    public void setEx_usd(double ex_usd) {
        this.ex_usd = ex_usd;
    }

    public double getEx_mex() {
        return ex_mex;
    }

    public void setEx_mex(double ex_mex) {
        this.ex_mex = ex_mex;
    }

    public double getEx_jap() {
        return ex_jap;
    }

    public void setEx_jap(double ex_jap) {
        this.ex_jap = ex_jap;
    }

    public double getEx_gbp() {
        return ex_gbp;
    }

    public void setEx_gbp(double ex_gbp) {
        this.ex_gbp = ex_gbp;
    }

    public double getEx_aus() {
        return ex_aus;
    }

    public void setEx_aus(double ex_aus) {
        this.ex_aus = ex_aus;
    }
}
