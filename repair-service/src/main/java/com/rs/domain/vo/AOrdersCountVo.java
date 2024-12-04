package com.rs.domain.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class AOrdersCountVo implements Serializable {
    private Integer orderStatus1;
    private Integer orderStatus2;
    private Integer orderStatus3;
    private Integer totalOrder;

    private Integer lastMonthOrderCount;
    private Integer currentMonthOrderCount;
    private Integer currentWeekOrderCount;
    private Integer yesterdayOrderCount;
    private Integer todayOrderCount;

    private Integer historyOrderType1;
    private Integer historyOrderType2;
    private Integer historyOrderType3;
    private Integer historyOrderType4;

    private Integer orderType1;
    private Integer orderType2;
    private Integer orderType3;
    private Integer orderType4;

    private Boolean serverStatus;
    private String orderNotice;
    private String stopNotice;
    private String staffNotice;
}
