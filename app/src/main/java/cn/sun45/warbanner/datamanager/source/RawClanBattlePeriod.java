package cn.sun45.warbanner.datamanager.source;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import cn.sun45.warbanner.document.db.source.ClanWarModel;

public class RawClanBattlePeriod {
    public int clan_battle_id;
    public int release_month;
    public String start_time;
    public String end_time;

//    public ClanBattlePeriod transToClanBattlePeriod(){
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd H:mm:ss");
//        return new ClanBattlePeriod(
//                clan_battle_id,
//                LocalDateTime.parse(start_time, formatter),
//                LocalDateTime.parse(end_time, formatter)
//        );
//    }

    public void set(ClanWarModel model) {
        model.setId(clan_battle_id);
        model.setStartdate(start_time);
        model.setEnddate(end_time);
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(new SimpleDateFormat("yyyy/MM/dd").parse(start_time));
            calendar.add(Calendar.DATE, 3);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        model.setDate(year + ((month < 10 ? "0" : "") + month));
    }
}
