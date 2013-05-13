package de.bitnoise.sonferenz.web.pages.schedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Init2013 {

	public static Timetable get() {
		Timetable table = new Timetable();
		Room r1 = new Room("Foyer");
		Room r2 = new Room("Besprecher");
		table.rooms.add(r1);
		table.rooms.add(r2);
		table.days.add(createDay1(r1, r2));
		table.days.add(createDay2(r1, r2));
		return table;
	}

	private static Day createDay2(Room r1, Room r2) {
		Day day = new Day();
		day.heading = "Freitag, 22. Juni";
		day.startTime = d("13:30");
		return day;
	}

	static Date d(String str) {
		try {
			return new SimpleDateFormat("HH:mm").parse(str);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	private static Day createDay1(Room r1, Room r2) {
		Day day = new Day();
		day.heading = "Samstag, 23. Juni";
		day.startTime = d("09:00");
		return day;
	}

}
