package net.core.db.interfaces;

import net.core.models.TemplateMetric;

import java.util.List;

public interface ITemplateStorage {

    public void addTemplateMetric(String title, String query);

    public TemplateMetric getTemplateMetric(int id);

    public List<TemplateMetric> getTemplatMetrics();

    public void updateTemplMetric(int id, String title, String command, double minValue, double maxValue);

    public void addTemplMetric(String title, String command, double minValue, double maxValue);

    public void dellTemplMetric(int id);
}
