package org.example.nazar.util.time.datereformater;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JalaliStringToGregorianDateDigikala extends JalaliToGregorianDate {


    private static final Map<String, Integer> persianMonths = new HashMap<>();

    static {
        persianMonths.put("فروردین", 1);
        persianMonths.put("اردیبهشت", 2);
        persianMonths.put("خرداد", 3);
        persianMonths.put("تیر", 4);
        persianMonths.put("مرداد", 5);
        persianMonths.put("شهریور", 6);
        persianMonths.put("مهر", 7);
        persianMonths.put("آبان", 8);
        persianMonths.put("آذر", 9);
        persianMonths.put("دی", 10);
        persianMonths.put("بهمن", 11);
        persianMonths.put("اسفند", 12);
    }


    @Override
    public List<Integer> dateSplitter(String date) {
        // جدا کردن روز، ماه و سال از رشته ورودی
        String[] parts = date.split(" ");
        int day = Integer.parseInt(parts[0]);
        int month = persianMonths.get(parts[1]);
        int year = Integer.parseInt(parts[2]);
        return List.of(year, month, day);
    }
}
