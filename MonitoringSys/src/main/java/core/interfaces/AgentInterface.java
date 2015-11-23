package core.interfaces;

public interface AgentInterface {

    public boolean connect() ;

    public void disconnect();

    public double getCPU();

    public double getFreeDisk() ;

    public double getUsedHDD();

    public double getTotalHDD() ;

    public double getFreeRAM(String unit);

    public double getUsedRAM(String unit);

    public double getTotalRAM(String unit);
}
