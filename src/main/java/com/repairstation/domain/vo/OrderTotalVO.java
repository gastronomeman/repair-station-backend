package com.repairstation.domain.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderTotalVO implements Serializable {
    private Integer semesterOrderCount;
    private Integer lastMonthOrderCount;
    private Integer currentMonthOrderCount;
    private Integer currentWeekOrderCount;
    private Integer yesterdayOrderCount;
    private Integer todayOrderCount;

    private Integer orderType11;
    private Integer orderType12;

    private Integer orderType21;
    private Integer orderType22;

    private Integer orderType31;
    private Integer orderType32;

    private Integer orderType41;
    private Integer orderType42;
}
