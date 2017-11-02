package com.allen.rxhttputils.bean;

/**
 * Created by xiaoyao on 2017/11/2.
 */

public class DataBean {

    /**
     * expectedEarning : 8.85
     * canUseRateCount : 1
     * canUsePrincipalCount : 3
     */

    private double expectedEarning;
    private int canUseRateCount;
    private int canUsePrincipalCount;

    public double getExpectedEarning() {
        return expectedEarning;
    }

    public void setExpectedEarning(double expectedEarning) {
        this.expectedEarning = expectedEarning;
    }

    public int getCanUseRateCount() {
        return canUseRateCount;
    }

    public void setCanUseRateCount(int canUseRateCount) {
        this.canUseRateCount = canUseRateCount;
    }

    public int getCanUsePrincipalCount() {
        return canUsePrincipalCount;
    }

    public void setCanUsePrincipalCount(int canUsePrincipalCount) {
        this.canUsePrincipalCount = canUsePrincipalCount;
    }
}
