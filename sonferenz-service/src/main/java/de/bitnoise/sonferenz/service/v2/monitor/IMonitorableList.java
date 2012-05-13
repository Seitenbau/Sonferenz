package de.bitnoise.sonferenz.service.v2.monitor;

import java.util.List;

public interface IMonitorableList {
  List<IMonitorState> getStates();
}
