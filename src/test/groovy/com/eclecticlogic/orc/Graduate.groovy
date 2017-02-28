/*
 * Copyright (c) 2017 Eclectic Logic LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.eclecticlogic.orc;

import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kabram
 */
public class Graduate extends Student {

    private String major;
    private BigDecimal allowance;
    private List<String> subjects = new ArrayList<>();
    private List<Long> grades = new ArrayList<>();
    List<Character> courseGrades = new ArrayList<>();
    List<Date> courseDates = new ArrayList<>();
    List<Boolean> courseAudits = new ArrayList<>();
    Course course;
    YearMonth graduationDate;
    Character subjectGrade;
    LocalDate initiationDate;

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    @Orc(precision = 10, scale = 5)
    public BigDecimal getAllowance() {
        return allowance;
    }

    public void setAllowance(BigDecimal allowance) {
        this.allowance = allowance;
    }


    @OrcList(entryType = String.class, averageSize = 50)
    @Orc(length = 30)
    public List<String> getSubjects() {
        return subjects;
    }


    @OrcList(entryType = Long.class, averageSize = 25)
    public List<Long> getGrades() {
        return grades;
    }


    public Course mycoursework() {
        return course;
    }


    @OrcConverter(GraduationConverter)
    YearMonth getGraduationDate() {
        return graduationDate
    }

    @OrcList(entryType = Character.class, averageSize = 5)
    List<Character> getCourseGrades() {
        return courseGrades
    }

    @OrcList(entryType = Date.class, averageSize = 5)
    List<Date> getCourseDates() {
        return courseDates
    }

    @OrcList(entryType = Boolean.class, averageSize = 5)
    List<Boolean> getCourseAudits() {
        return courseAudits
    }

}
