package de.bitnoise.sonferenz.web.pages.admin.tabs;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

class UsersList extends Panel {

	List<String> users;

    public UsersList( String id, Set<String> users) {
      super(id );
      ArrayList<String> l = new ArrayList<String>(users);
      this.users = l;
    }

    @Override
    protected void onInitialize() {
      super.onInitialize();

      ListView<String> liste = new ListView<String>("userList", users) {
        @Override
        protected void populateItem(ListItem<String> item) {
          String object = item.getModel().getObject();
          item.add(new Label("name", object));
        }
      };
      add(new Label("userCount", Model.of(users.size())));
      add(liste);
    }

  }