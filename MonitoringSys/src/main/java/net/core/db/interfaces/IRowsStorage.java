package net.core.db.interfaces;


import net.core.models.HostEditRow;
import net.core.models.HostRow;
import net.core.models.MetricRow;
import net.core.models.ProblemRow;

import java.util.List;

public interface IRowsStorage {

    public List<HostRow> getHostRow();

    public List<HostEditRow> getHostEditRow();

    public List<MetricRow> getMetricRow(int hostId);

    public ProblemRow getProblem(int problemId);
}
