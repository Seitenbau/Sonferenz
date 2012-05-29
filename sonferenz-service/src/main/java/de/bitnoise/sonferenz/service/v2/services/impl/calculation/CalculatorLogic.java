package de.bitnoise.sonferenz.service.v2.services.impl.calculation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Supplier;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;

import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.CalcTalk;
import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.CollisionResult;
import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.Collisions;
import de.bitnoise.sonferenz.service.v2.services.impl.calculation.CalculationTalkImpl.Constraint;

public class CalculatorLogic {

  protected CalculationConfigurationImpl _cfg;
  protected CollistionResultImpl _result;

  public CalculatorLogic(CalculationConfigurationImpl theConfig) {
    _cfg = theConfig;
  }

  public CollisionResult calculateCollisions() {
    _result = new CollistionResultImpl();
    _result.init(_cfg.getTalks());

    for (CalculationTalkImpl talkA : _cfg.getTalks()) {
      for (CalculationTalkImpl talkB : _cfg.getTalks()) {
        if (talkA == talkB) {
          _result.set(talkA, talkB, new CollisionsStat(-1));
          break; // Nur 1. Teilmatrix errechnen
        }
        Collisions val = calcCollision(talkA, talkB);
        _result.set(talkA, talkB, val);
        _result.set(talkB, talkA, val);
      }
    }
    return _result;
  }

  private Collisions calcCollision(CalculationTalkImpl talkA, CalculationTalkImpl talkB) {
    List<Constraint> constraints = talkA.getConstraints();
    int weight = 0;
    List<ConstraintEvent> events = new ArrayList<ConstraintEvent>();
    for (Constraint c : constraints) {
      ConstraintEvent val = c.calc(_cfg, talkB);
      if (val == null) {
        continue;
      }
      events.add(val);
      if (val.getWeight() <= -1) {
        return new CollisionsStat(-1,events);
      }
      weight += val.getWeight();
      
    }
    return new CollisionsStat(weight,events);
  }

  public class CollistionResultImpl implements CollisionResult {

    Table<CalcTalk, CalcTalk, Collisions> _table = createTable();

    protected Table<CalcTalk, CalcTalk, Collisions> createTable() {
      // use LinkedHashMaps instead of HashMaps
      return Tables.newCustomTable(
          Maps.<CalcTalk, Map<CalcTalk, Collisions>> newLinkedHashMap(),
          new Supplier<Map<CalcTalk, Collisions>>() {
            public Map<CalcTalk, Collisions> get() {
              return Maps.newLinkedHashMap();
            }
          });
    }

    public void set(CalculationTalkImpl talkA, CalculationTalkImpl talkB, Collisions val) {
      _table.put(talkA, talkB, val);
    }

    public void init(List<CalculationTalkImpl> talks) {
      for (CalcTalk talkA : talks) {
        for (CalcTalk talkB : talks) {
          _table.put(talkA, talkB, new CollisionsStat(0));
        }
      }
    }

    @Override
    public Collisions getCollisions(CalcTalk talk1, CalcTalk talk2) {
      return _table.get(talk1, talk2);
    }

    @Override
    public String toString() {
      StringBuffer sb = new StringBuffer();
      Set<CalcTalk> cols = _table.columnKeySet();
      sb.append("  \t| ");
      for (CalcTalk col : cols) {
        String colPadded = StringUtils.leftPad(col.toString(), 5);
        sb.append(colPadded);
        sb.append("\t| ");
      }
      sb.append("\r\n");
      Set<CalcTalk> rows = _table.rowKeySet();
      for (CalcTalk row : rows) {
        sb.append(row);
        sb.append("\t| ");
        for (CalcTalk col : cols) {
          Collisions value = _table.get(row, col);
          String valuePadded = StringUtils.leftPad(value.toString(), 5);
          sb.append(valuePadded);
          sb.append("\t| ");
        }
        sb.append("\r\n");
      }
      return sb.toString();
    }

  }
}
