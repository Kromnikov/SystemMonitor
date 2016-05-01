package net.core.db;


public interface IHomePageStorage {

    public void addToFavorites(int host, int metric, String user);

    public void dellFromFavorites(int favoritesId);

    public int hostsProblemsCount();

    public int hostsSuccesCount();

    public int metricsProblemCount();

    public int metricsSuccesCount();
}
