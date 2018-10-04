package com.eclecticlogic.orc;

import java.util.ArrayList;
import java.util.List;

public class ArrayTest {

    String name;

    List<String> grades = new ArrayList<>();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @OrcList(entryType = String.class, averageSize = 1200, elementSize = 30)
    public List<String> getGrades() {
        return grades;
    }

    public void setGrades(List<String> grades) {
        this.grades = grades;
    }
}
