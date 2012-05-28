package de.bitnoise.sonferenz.service.v2.services.impl.calculation;

public class OneSlot implements SlotItem {

  String _name;

  public OneSlot(String name) {
    _name = name;
  }

  @Override
  public String toString() {
    return _name;
  }
}