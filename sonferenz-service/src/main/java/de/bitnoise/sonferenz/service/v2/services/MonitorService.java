package de.bitnoise.sonferenz.service.v2.services;

import java.util.List;

import de.bitnoise.sonferenz.service.v2.monitor.IMonitorState;

public interface MonitorService {
  List<IMonitorState> getStates();
}
