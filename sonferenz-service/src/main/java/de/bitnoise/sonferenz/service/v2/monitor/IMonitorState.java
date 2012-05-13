package de.bitnoise.sonferenz.service.v2.monitor;

import java.io.Serializable;

public interface IMonitorState extends Serializable {
  String getMonitorGroup();
  
	String getMonitorName();

	MonitorStateEnum getMonitorState();

	String getMonitorStateDetails();
}
