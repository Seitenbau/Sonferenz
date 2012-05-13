package de.bitnoise.sonferenz.service.v2.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.bitnoise.sonferenz.service.v2.monitor.IMonitorState;
import de.bitnoise.sonferenz.service.v2.monitor.IMonitorable;
import de.bitnoise.sonferenz.service.v2.monitor.IMonitorableList;
import de.bitnoise.sonferenz.service.v2.services.MonitorService;

@Service
public class MonitorServiceImpl implements MonitorService {

  @Autowired(required = false)
  List<IMonitorable> services;

  @Autowired(required = false)
  List<IMonitorableList> services2;

  @Override
  public List<IMonitorState> getStates() {
    List<IMonitorState> states = new ArrayList<IMonitorState>();
    if (services != null) {
      for (IMonitorable service : services) {
        states.add(service.getState());
      }
    }
    if (services2 != null) {
      for (IMonitorableList service : services2) {
        states.addAll(service.getStates());
      }
    }
    return states;
  }

}
