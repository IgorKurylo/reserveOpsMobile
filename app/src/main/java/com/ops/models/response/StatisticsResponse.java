package com.ops.models.response;

import com.google.gson.annotations.SerializedName;
import com.ops.models.Statistic;

public class StatisticsResponse {
    @SerializedName("statistics")
    private Statistic statistic;

    public StatisticsResponse(Statistic statistic) {
        this.statistic = statistic;
    }

    public Statistic getStatistic() {
        return statistic;
    }

    public void setStatistic(Statistic statistic) {
        this.statistic = statistic;
    }
}
