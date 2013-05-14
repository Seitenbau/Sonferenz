package de.bitnoise.sonferenz.web.pages.schedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;

public class NewSchedulePanel extends Panel
{

  public NewSchedulePanel(String id)
  {
    super(id);
  }

 

  @Override
  protected void onInitialize() {
	  super.onInitialize();
	  
	  Timetable table =  Init2013.get();
	  // FIXME
//	  RepeatingView days = new RepeatingView("days");
  }

}
