package org.example.nazar.util.time.datereformater;

import ir.huri.jcal.JalaliCalendar;

import java.time.LocalDate;
import java.util.GregorianCalendar;


public abstract class JalaliToGregorianDate implements IDateReFormater {
    @Override
    public LocalDate reFormater(Integer year, Integer month, Integer day) {

        // ساخت شیء JalaliCalendar و تبدیل به GregorianCalendar
        JalaliCalendar jalaliCalendar = new JalaliCalendar(year, month, day);
        GregorianCalendar gregorianCalendar = jalaliCalendar.toGregorian();

        // تبدیل GregorianCalendar به LocalDate
        return LocalDate.of(
                gregorianCalendar.get(GregorianCalendar.YEAR),
                gregorianCalendar.get(GregorianCalendar.MONTH) + 1, // ماه‌ها از 0 شروع می‌شوند
                gregorianCalendar.get(GregorianCalendar.DAY_OF_MONTH)
        );

    }


}
