package net.core.db.interfaces;


import net.core.models.MetricProblem;

import java.text.ParseException;
import java.util.List;

public interface IMetricProblemStorage {

    public List<MetricProblem> getMetricProblems(int hostId) throws ParseException;

    public List<MetricProblem> getMetricProblems(int hostId, int metricId) throws ParseException;

    public List<MetricProblem> getMetricProblems() throws ParseException;

    public void setResolvedMetric(int id);

    public void setResolvedMetric();

    public long getMetricNotResolvedLength();

    public long getMetricNotResolvedLength(int hostId);
}
