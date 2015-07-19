package barqsoft.footballscores;

import android.content.Context;

/**
 * Created by yehya khaled on 3/3/2015.
 */
public class Utilies {
    public static final int SERIE_A = 357;
    public static final int PREMIER_LEGAUE = 354;
    public static final int CHAMPIONS_LEAGUE = 362;
    public static final int PRIMERA_DIVISION = 358;
    public static final int BUNDESLIGA = 351;

    public static String getLeague(Context context, int leagueNum) {
        switch (leagueNum) {
            case SERIE_A:
                return context.getString(R.string.seriaa);
            case PREMIER_LEGAUE:
                return context.getString(R.string.premierleague);
            case CHAMPIONS_LEAGUE:
                return context.getString(R.string.champions_league);
            case PRIMERA_DIVISION:
                return context.getString(R.string.primeradivison);
            case BUNDESLIGA:
                return context.getString(R.string.bundesliga);
            default:
                return context.getString(R.string.unknown_league);
        }
    }

    public static String getMatchDay(Context context, int matchDay, int leagueNum) {
        if (leagueNum == CHAMPIONS_LEAGUE) {
            if (matchDay <= 6) {
                return context.getString(R.string.group_stage_text) + ", " +
                        context.getString(R.string.matchday_text) + " : " +
                        String.format("%d", matchDay);
            } else if (matchDay == 7 || matchDay == 8) {
                return context.getString(R.string.first_knockout_round);
            } else if (matchDay == 9 || matchDay == 10) {
                return context.getString(R.string.quarter_final);
            } else if (matchDay == 11 || matchDay == 12) {
                return context.getString(R.string.semi_final);
            } else {
                return context.getString(R.string.final_text);
            }
        } else {
            //in order to support rtl and other languages use String.format().
            return context.getString(R.string.matchday_text) + " : " + String.format("%d", matchDay);
        }
    }

    public static String getScores(int home_goals, int awaygoals) {
        if (home_goals < 0 || awaygoals < 0) {
            return " - ";
        } else {
            //in order to support rtl and other languages use String.format().
            return String.format("%d", home_goals) + " - " + String.format("%d", awaygoals);
        }
    }

    public static int getTeamCrestByTeamName(Context context, String teamname) {
        if (context.getString(R.string.arsenal).equals(teamname)) {
            return R.drawable.arsenal;
        } else if (context.getString(R.string.manchester).equals(teamname)) {
            return R.drawable.manchester_united;
        } else if (context.getString(R.string.swansea).equals(teamname)) {
            return R.drawable.swansea_city_afc;
        } else if (context.getString(R.string.leichester).equals(teamname)) {
            return R.drawable.leicester_city_fc_hd_logo;
        } else if (context.getString(R.string.everton).equals(teamname)) {
            return R.drawable.everton_fc_logo1;
        } else if (context.getString(R.string.west_ham).equals(teamname)) {
            return R.drawable.west_ham;
        } else if (context.getString(R.string.tottenham).equals(teamname)) {
            return R.drawable.tottenham_hotspur;
        } else if (context.getString(R.string.west_bronwich).equals(teamname)) {
            return R.drawable.west_bromwich_albion_hd_logo;
        } else if (context.getString(R.string.sunderland).equals(teamname)) {
            return R.drawable.sunderland;
        } else if (context.getString(R.string.stoke).equals(teamname)) {
            return R.drawable.stoke_city;
        } else {
            return R.drawable.no_icon;
        }
    }
}
