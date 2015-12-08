package core.models;

public class InstanceMetric {

    private int id;

    private int tempMetrcId;

    private int hostId;

    private String title;

    private String command;

    private double minValue;

    private double maxValue;

    public InstanceMetric() {
    }

    public InstanceMetric(int id, int tempMetrcId, int hostId) {
        this.id = id;
        this.tempMetrcId = tempMetrcId;
        this.hostId = hostId;
    }

    public InstanceMetric(int id, int tempMetrcId, int hostId, double minValue, double maxValue) {
        this.id = id;
        this.tempMetrcId = tempMetrcId;
        this.hostId = hostId;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public InstanceMetric(int id, int tempMetrcId, int hostId, String title, String command, double minValue, double maxValue) {
        this.id = id;
        this.tempMetrcId = tempMetrcId;
        this.hostId = hostId;
        this.title = title;
        this.command = command;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public int getTempMetrcId() {
        return tempMetrcId;
    }

    public void setTempMetrcId(int tempMetrcId) {
        this.tempMetrcId = tempMetrcId;
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

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public int getHostId() {
        return hostId;
    }

    public void setHostId(int hostId) {
        this.hostId = hostId;
    }
}
