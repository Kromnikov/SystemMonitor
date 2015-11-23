package core.factory;

import core.interfaces.AgentInterface;

public class AgentFactory {

    private AgentInterface ac;

    public AgentFactory(AgentInterface agentConnection) {
        this.ac = agentConnection;
    }

    public boolean connect() {
        return ac.connect();
    }

    public void disconnect(){ac.disconnect();}

    public double getCPU() {
        return ac.getCPU();
    }

    public double getFreeDisk() {
        return ac.getFreeDisk();
    }

    public double getUsedHDD() {
        return ac.getUsedHDD();
    }

    public double getTotalHDD() {
        return ac.getTotalHDD();
    }

    public double getFreeRAM(String unit) {
        return ac.getFreeRAM(unit);
    }

    public double getUsedRAM(String unit) {
        return ac.getUsedRAM(unit);
    }

    public double getTotalRAM(String unit) {
        return ac.getTotalRAM(unit);
    }
}
