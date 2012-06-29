package de.bitnoise.sonferenz.service.v2.services;

public interface StatisticService
{
  enum ResourceEvent {
    OPEN("open"),
    DOWNLOAD("download"),
    ACTIVE_5MINUTES("5m"),
    ACTIVE_15MINUTES("15m"),
    ACTIVE_25MINUTES("25m"),
    ACTIVE_35MINUTES("35m"),
    ACTIVE_45MINUTES("45m"),
    ACTIVE_55MINUTES("55m"),
    ACTIVE_65MINUTES("65m");
    String _key;
    ResourceEvent(String key) {
      _key = key;
    }
    public String getKey() {
      return _key;
    }
  }
  void incrementHit(String objectId, ResourceEvent span);
}
