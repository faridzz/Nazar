package org.example.nazar.util.time.datereformater;

import java.time.LocalDate;
import java.util.List;

public interface IDateReFormater {
    LocalDate reFormater(Integer year, Integer month, Integer day);

    List<Integer> dateSplitter(String date);
}
