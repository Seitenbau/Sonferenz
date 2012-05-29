//package de.bitnoise.sonferenz.service.v2.services.impl.calculation;
//
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.rules.ExpectedException;
//
//import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.CalculationConfiguration;
//import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.CalcTalk;
//import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.CalcUser;
//import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.CollisionResult;
//import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.ConstraintFunction;
//import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.Slot;
//import de.bitnoise.sonferenz.service.v2.services.impl.calculation.CalculateTimeTableServiceImpl;
//
//import static org.fest.assertions.Assertions.*;
//
//public class CalculateTimeTableServiceImplTest {
//
//  CalculateTimeTableServiceImpl sut = new CalculateTimeTableServiceImpl();
//
//  @Rule
//  public ExpectedException exception = ExpectedException.none();
//
//  @Test
//  public void sample() {
//    CalculationConfiguration cfg = sut.createConfig();
//
//    // execute Slot
//    SlotReference slot1 = cfg.createSlot("14:00", 2);
//    SlotReference slot2 = cfg.createSlot("15:30", 2);
//
//    // verify slot
//    assertThat(slot1).isNotNull();
//    assertThat(slot2).isNotNull();
////    assertThat(slot1.getRooms()).isEqualTo(2);
////    assertThat(slot2.getRooms()).isEqualTo(2);
////
////    assertThat(slot1.getRoom(0)).isNotNull();
////    assertThat(slot1.getRoom(1)).isNotNull();
////    assertThat(slot1.getRoom(0).toString()).isEqualTo("0 : 14:00");
////    assertThat(slot1.getRoom(1).toString()).isEqualTo("1 : 14:00");
////
////    assertThat(slot2.getRoom(0)).isNotNull();
////    assertThat(slot2.getRoom(1)).isNotNull();
////    assertThat(slot2.getRoom(0).toString()).isEqualTo("0 : 15:30");
////    assertThat(slot2.getRoom(1).toString()).isEqualTo("1 : 15:30");
//
//    // exec add talk
//    CalcTalk vort1 = cfg.addTalk("v1");
//    CalcTalk vort2 = cfg.addTalk("v2");
//    CalcTalk vort3 = cfg.addTalk("v3");
//    CalcTalk vort4 = cfg.addTalk("v4");
//
//    // verify add talk
//    assertThat(vort1).isNotNull();
//    assertThat(vort2).isNotNull();
//    assertThat(vort3).isNotNull();
//    assertThat(vort4).isNotNull();
//
//    assertThat(vort1.toString()).isEqualTo("v1");
//    assertThat(vort2.toString()).isEqualTo("v2");
//    assertThat(vort3.toString()).isEqualTo("v3");
//    assertThat(vort4.toString()).isEqualTo("v4");
//
//    // exec add user
//    CalcUser u1 = cfg.addUser("u1");
//    CalcUser u2 = cfg.addUser("u2");
//    CalcUser u3 = cfg.addUser("u3");
//    CalcUser u4 = cfg.addUser("u4");
//    CalcUser u5 = cfg.addUser("u5");
//    CalcUser u6 = cfg.addUser("u6");
//    CalcUser u7 = cfg.addUser("u7");
//    CalcUser u8 = cfg.addUser("u8");
//
//    // verify add user
//    assertThat(u1).isNotNull();
//    assertThat(u2).isNotNull();
//    assertThat(u3).isNotNull();
//    assertThat(u4).isNotNull();
//    assertThat(u1).isNotNull();
//    assertThat(u2).isNotNull();
//    assertThat(u3).isNotNull();
//    assertThat(u4).isNotNull();
//    assertThat(u1.toString()).isEqualTo("u1");
//    assertThat(u2.toString()).isEqualTo("u2");
//    assertThat(u3.toString()).isEqualTo("u3");
//    assertThat(u4.toString()).isEqualTo("u4");
//    assertThat(u5.toString()).isEqualTo("u5");
//    assertThat(u6.toString()).isEqualTo("u6");
//    assertThat(u7.toString()).isEqualTo("u7");
//    assertThat(u8.toString()).isEqualTo("u8");
//
//    CollisionResult result = sut.calculateCollisions(cfg);
//    
//    sut.calculateTalkorder(cfg,result);
//  }
//  
//}
