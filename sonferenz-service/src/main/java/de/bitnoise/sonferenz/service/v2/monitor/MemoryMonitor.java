package de.bitnoise.sonferenz.service.v2.monitor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class MemoryMonitor implements IMonitorableList {

  @Override
  public List<IMonitorState> getStates() {
    List<IMonitorState> list = new ArrayList<IMonitorState>();

    Runtime rt = Runtime.getRuntime();
    long totalMemory = rt.totalMemory();
    long freeMemory = rt.freeMemory();
    long usedMemory = totalMemory - freeMemory;

    list.add(new MonitorState("MEM", "TotalMemory", MonitorStateEnum.OK, MB(totalMemory)));
    list.add(new MonitorState("MEM", "FreeMemory", MonitorStateEnum.OK, MB(freeMemory)));
    list.add(new MonitorState("MEM", "UsedMemory", MonitorStateEnum.OK, MB(usedMemory)));

    return list;
  }

  private String MB(long bytes) {
    return Long.toString(bytes / 1024 / 1024) + "MB";
  }

}
