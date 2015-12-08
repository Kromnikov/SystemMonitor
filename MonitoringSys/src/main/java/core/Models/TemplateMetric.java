package core.models;


public class TemplateMetric {

    private int id;

    private String title;

    private String command;

    public TemplateMetric() {
    }

    public TemplateMetric(int id, String title, String command) {
        this.id=id;
        this.title = title;
        this.command = command;
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

}
