package org.soa.reservation_service.service.interfaces;

import org.soa.reservation_service.dto.Revenue;

import java.util.List;

public interface IDashboardService {

    List<Revenue> incomeByService();

    List<Revenue> incomeByServiceCategory();

    List<Revenue> statusStatistic();

    List<Revenue> dailyRevenue();

    List<Revenue> weeklyRevenue();

    List<Revenue> monthlyRevenue();

    List<Revenue> statisticPerMonth();
}
