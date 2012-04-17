package de.bitnoise.sonferenz;

public interface Right
{

  interface User
  {
    String Create = "user.create";
    String List = "user.list";
    String Edit = "user.edit";
  }

  interface Admin
  {
    String ViewCalculation = "admin.calculation.view";
    String StartCalculation = "admin.calculation.start";
    String Configure = "admin.config.global";
  }
  
  interface Actions {
    String InviteUser = "action.invite.newUser";
  }

  interface Conference
  {
    String List = "conference.list";
    String Edit = "conference.edit";
    String Create = "conference.create";
  }

  interface Talk
  {
    String List = "talks.list";
    String Edit = "talks.edit";
    String Create = "talks.create";
  }

  interface Whish
  {
    String List = "whish.list";
    String Edit = "whish.edit";
    String Create = "whish.create";
  }

  interface Vote
  {
    String canVote = "vote.canVote";
  }
}
