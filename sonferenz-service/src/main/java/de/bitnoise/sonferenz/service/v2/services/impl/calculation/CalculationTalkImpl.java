package de.bitnoise.sonferenz.service.v2.services.impl.calculation;

import java.util.ArrayList;
import java.util.List;

import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.CalcTalk;
import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.CalcUser;
import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.Collisions;
import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.ConstraintFunction;

public class CalculationTalkImpl implements CalcTalk {

  protected Object _externalRefernece;
  protected List<Constraint> _constraints = new ArrayList<Constraint>();
  protected List<CalcUser> _users = new ArrayList<CalcUser>();

  public CalculationTalkImpl(Object reference) {
    _externalRefernece = reference;
  }

  @Override
  public Object getId() {
    return _externalRefernece;
  }

  @Override
  public String toString() {
    return _externalRefernece.toString();
  }

  @Override
  public void addConstraint(ConstraintFunction func, CalcUser user) {
    _users.add(user);
    _constraints.add(new Constraint(func, user));
  }

  public List<Constraint> getConstraints() {
    return _constraints;
  }
  
  public boolean containsUser(CalcUser user) {
    return _users.contains(user);
  }

  public class Constraint {

    ConstraintFunction _func;
    CalcUser _user;

    public Constraint(ConstraintFunction func, CalcUser user) {
      _func = func;
      _user = user;
    }

    public Integer calc(CalculationConfigurationImpl cfg, CalculationTalkImpl talkB) {
      return _func.calculate(talkB,_user);
    }

  }


}
