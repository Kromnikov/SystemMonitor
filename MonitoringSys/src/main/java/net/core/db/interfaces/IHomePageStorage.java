package net.core.db.interfaces;


import net.core.models.Favorites;

import java.util.List;

public interface IHomePageStorage {

    public void addToFavorites(int host, int metric, String user);

    public void dellFromFavorites(int favoritesId);

    public int hostsProblemsCount();

    public int hostsSuccesCount();

    public int metricsProblemCount();

    public int metricsSuccesCount();

    public List<Favorites> getFavoritesRow(String name);
}
