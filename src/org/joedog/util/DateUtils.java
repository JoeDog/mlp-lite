package org.joedog.util;
/**
 * Copyright (C) 2013-2016
 * Jeffrey Fulmer - <jeff@joedog.org>, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *--
 */
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.time.ZoneId;
import java.time.LocalDate;
import java.text.ParseException;

public final class DateUtils {
  private static String [] formats = new String[]{
    "MM-dd-yy", 
    "MM-dd-yyyy", 
    "MM/dd/yy", 
    "MM/dd/yyyy",
    "yyyy-MM-dd"
  };
  
  public static boolean isValid(String date) {
    for (int i = 0; i < DateUtils.formats.length; i++) {
      DateFormat sdf = new SimpleDateFormat(DateUtils.formats[i]);
      sdf.setLenient(false);
      try {
        sdf.parse(date);
        return true;
      } catch (ParseException e) {
        continue;
      }
    }
    return false;
  }

  public static DateTimeFormatter getFormat(String date) {
    for (int i = 0; i < DateUtils.formats.length; i++) {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateUtils.formats[i], Locale.ENGLISH);
      try {
        formatter.parse(date);
        return formatter;
      } catch (Exception e) {
        continue;
      }
    }
    return null;
  }

  public static Date fromString(String date) {
    ZoneId            tzone  = ZoneId.systemDefault();
    DateTimeFormatter format = DateUtils.getFormat(date);
    if (format == null) {
      return null;
    }
    LocalDate         local  = LocalDate.parse(date, format);
    return Date.from(local.atStartOfDay(tzone).toInstant());
  }

  public static String now() {
    return DateUtils.now("MM-dd-yyyy HH:mm:ss");
  }

  public static String now(String format) {
    DateFormat df = new SimpleDateFormat(format);
    Date today = Calendar.getInstance().getTime();
    return df.format(today);
  }

  public static String monthToString(int month) {
    return DateUtils.monthToString(month, false);
  }

  public static int month() {
    DateFormat df = new SimpleDateFormat("MM");
    Date today = Calendar.getInstance().getTime();
    int  month = Integer.parseInt(df.format(today));
    return month;
  }

  public static int day() {
    DateFormat df = new SimpleDateFormat("dd");
    Date today = Calendar.getInstance().getTime();
    int  day   = Integer.parseInt(df.format(today));
    return day;
  }

  public static String dayOfWeek() {
    return DateUtils.dayOfWeek(false);
  }

  public static String dayOfWeek(boolean full) {
    String     fmt   = (full==true) ? "EEEE" : "E";
    DateFormat df    = new SimpleDateFormat(fmt);
    Date       today = Calendar.getInstance().getTime();
    return df.format(today);
  }

  public static int weekDay(boolean iso) {
    String day = DateUtils.dayOfWeek(false);
    return DateUtils.weekDay(day, iso);
  }

  public static int weekDay(String day, boolean iso) {
    switch(day) {
      case "Mon": 
        return (iso == true) ? 1 : 0;
      case "Tue": 
        return (iso == true) ? 2 : 1;
      case "Wed": 
        return (iso == true) ? 3 : 2;
      case "Thu": 
        return (iso == true) ? 4 : 3;
      case "Fri": 
        return (iso == true) ? 5 : 4;
      case "Sat": 
        return (iso == true) ? 6 : 5;
      case "Sun": 
        return (iso == true) ? 7 : 6;
      default:
        return -1;
    } 
  }

  public static String monthToString(int month, boolean full) {
    switch (month) {
      case 1:
        if (full) return "January";
        return "Jan";
      case 2:
        if (full) return "February";
        return "Feb";
      case 3:
        if (full) return "March";
        return "Mar";
      case 4:
        if (full) return "April";
        return "Apr";
      case 5:
        if (full) return "May";
        return "May";
      case 6:
        if (full) return "June";
        return "Jun";
      case 7:
        if (full) return "July";
        return "Jul";
      case 8:
        if (full) return "August";
        return "Aug";
      case 9:
        if (full) return "September";
        return "Sep";
      case 10:
        if (full) return "October";
        return "Oct";
      case 11:
        if (full) return "November";
         return "Nov";
      case 12:
        if (full) return "December";
         return "Dec";
      default: // WTF?
         return "";
    }
  }

  public static String dayToString(int day, boolean full) {
    switch (day) {
      case 1:
        if (full) return "January";
        return "Jan";
      case 2:
        if (full) return "February";
        return "Feb";
      case 3:
        if (full) return "March";
        return "Mar";
      case 4:
        if (full) return "April";
        return "Apr";
      case 5:
        if (full) return "May";
        return "May";
      case 6:
        if (full) return "June";
        return "Jun";
      case 7:
        if (full) return "July";
        return "Jul";
      default:
        return "";
    }
  }
}
