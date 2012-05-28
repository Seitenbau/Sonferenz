package de.bitnoise.sonferenz.service.v2.services.impl.calculation;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
    for (Constraint c : constraints) {
      Integer val = c.calc(_cfg, talkB);
      if (val == null) {
        continue;
      }
      if (val <= -1) {
        return new CollisionsStat(-1);
      }
      weight += val;
    }
    return new CollisionsStat(weight);
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
        sb.append(col);
        sb.append("\t| ");
      }
      sb.append("\r\n");
      Set<CalcTalk> rows = _table.rowKeySet();
      for (CalcTalk row : rows) {
        sb.append(row);
        sb.append("\t| ");
        for (CalcTalk col : cols) {
          sb.append(_table.get(row, col));
          sb.append("\t| ");
        }
        sb.append("\r\n");
      }
      return sb.toString();
    }

  }
}
