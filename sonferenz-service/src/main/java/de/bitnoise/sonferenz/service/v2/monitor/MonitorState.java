package de.bitnoise.sonferenz.service.v2.monitor;

public class MonitorState implements IMonitorState {

  String _name;
  MonitorStateEnum _state;
  String _details;
  String _group;

  public MonitorState(String name, MonitorStateEnum state) {
    _name = name;
    _state = state;
    _details = null;
    _group = null;
  }

  public MonitorState(String name, MonitorStateEnum state, String details) {
    _name = name;
    _state = state;
    _details = details;
    _group = null;
  }

  public MonitorState(String group, String name, MonitorStateEnum state) {
    _name = name;
    _state = state;
    _details = null;
    _group = group;
  }

  public MonitorState(String group, String name, MonitorStateEnum state, String details) {
    _name = name;
    _state = state;
    _details = details;
    _group = group;
  }

  @Override
  public String getMonitorName() {
    return _name;
  }

  @Override
  public MonitorStateEnum getMonitorState() {
    return _state;
  }

  @Override
  public String getMonitorStateDetails() {
    return _details;
  }

  @Override
  public String getMonitorGroup() {
    return _group;
  }

}
