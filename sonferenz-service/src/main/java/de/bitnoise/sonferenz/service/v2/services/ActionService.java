package de.bitnoise.sonferenz.service.v2.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import de.bitnoise.sonferenz.model.ActionModel;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.service.v2.actions.ActionResult;
import de.bitnoise.sonferenz.service.v2.actions.ActionState;
import de.bitnoise.sonferenz.service.v2.actions.Aktion;
import de.bitnoise.sonferenz.service.v2.actions.KonferenzAction;

public interface ActionService
{

  Aktion loadAction(String action, String token);

  Page<ActionModel> getUserActions(PageRequest request, UserModel user);

  void execute(ActionState data);

  ActionResult createAction(KonferenzAction action, ActionState data);

  Page<ActionModel> getAllUserActions(PageRequest request, UserModel user);


}
