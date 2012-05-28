//package de.bitnoise.sonferenz.service.v2.services.impl.calculation;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.RoomAtPointInTime;
//import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.Slot;
//
//public class CalculationSlot implements Slot {
//
//  protected Object _externalRef;
//  List<RoomAtPointInTime> _rooms = new ArrayList<RoomAtPointInTime>();
//
//  public CalculationSlot(Object reference, Integer parallelCount) {
//    _externalRef = reference;
//    for (int i = 0; i < parallelCount; i++) {
//      _rooms.add(new Room("" + i + " : " + reference.toString()));
//    }
//  }
//
//  @Override
//  public Integer getRooms() {
//    return _rooms.size();
//  }
//
//  @Override
//  public RoomAtPointInTime getRoom(int i) {
//    return _rooms.get(i);
//  }
//
//  class Room implements RoomAtPointInTime {
//
//    protected String _text;
//
//    public Room(String string) {
//      _text = string;
//    }
//
//    @Override
//    public String toString() {
//      return _text;
//    }
//
//  }
//
//}
