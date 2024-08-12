package org.example.nazar.util.time.datereformater;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import ir.huri.jcal.JalaliCalendar;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JalaliToGregorianDate implements IDateReFormater {
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

    @Override
    public List<Integer> dateSplitter(String date) {
        // شکستن رشته تاریخ به اجزاء سال، ماه و روز
        String[] dateSplitter = date.split("/");
        return Arrays.stream(dateSplitter)
                .map(Integer::parseInt)
                .toList();
    }


//    public static void main(String[] args) {
//        JalaliToGregorianDate converter = new JalaliToGregorianDate();
//
//        // تست متد dateSplitter
//        String jalaliDateStr = "1403/5/9";
//        List<Integer> dateParts = converter.dateSplitter(jalaliDateStr);
//        System.out.println("Date Parts: " + dateParts);
//
//        // تست متد reFormater
//        LocalDate gregorianDate = converter.reFormater(dateParts.get(0), dateParts.get(1), dateParts.get(2));
//        System.out.println("Gregorian Date: " + gregorianDate);
//    }
}
