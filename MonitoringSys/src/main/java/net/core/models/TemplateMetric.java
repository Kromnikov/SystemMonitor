package net.core.models;


public class TemplateMetric {

    private int id;

    private String title;

    private String command;

    private double min_value;

    private double max_value;

    public TemplateMetric() {
    }

    public TemplateMetric(int id, String title, String command,double min_value, double max_value) {
        this.id=id;
        this.title = title;
        this.command = command;
        this.max_value = max_value;
        this.min_value=min_value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public double getMin_value() {
        return min_value;
    }

    public void setMin_value(double min_value) {
        this.min_value = min_value;
    }

    public double getMax_value() {
        return max_value;
    }

    public void setMax_value(double max_value) {
        this.max_value = max_value;
    }
}
