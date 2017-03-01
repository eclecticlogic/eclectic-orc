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

package com.eclecticlogic.orc.impl

import com.eclecticlogic.orc.Course
import com.eclecticlogic.orc.Graduate
import com.eclecticlogic.orc.Factory
import com.eclecticlogic.orc.OrcHandle
import com.eclecticlogic.orc.OrcWriter
import com.eclecticlogic.orc.Schema
import com.eclecticlogic.orc.Teacher
import org.apache.hadoop.fs.Path
import org.apache.orc.CompressionKind
import org.apache.orc.TypeDescription
import org.testng.annotations.Test

import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDate
import java.time.YearMonth

/**
 * Created by kabram
 */
@Test
class TestBootstrap {

    void testOrcWriting() {
        Schema schema = Factory.createSchema(Graduate)
                .column { it.level }
                .column {it.club }
                .column { it.power }
                .column { it.name }
                .column { it.age }
                .column('money') { it.allowance }
                .column('subjects') { it.subjects }
                .column('gpa') { it.grades }
                .column('subject') { it.course.name }
                .column('teacher') { it.course.teacher.name }
                .column('tenured') { it.course.teacher.tenure }
                .column { it.course.teacher.startMonth }
                .column('advisor') { it.mycoursework().teacher.name }
                .column { it.courseGrades }
                .column { it.courseAudits }
                .column { it.courseDates }
                .column('initiation') { it.initiationDate }

        OrcHandle handle = Factory.createWriter(schema)
        List<Graduate> list = []
        list << new Graduate(name: 'abc', age: 10, allowance: 150.0).with {
            it.subjects << 'english'
            it.subjects << 'history'
            it.subjects << 'math'
            it.grades << 2L
            it.grades << 3L
            it.grades << 4L
            it.subjectGrade = 'A'
            it.course = new Course(name: 'Mathematics', teacher: new Teacher(name: 'John Brewer'))
            it.graduationDate = YearMonth.of(2020, 12)
            it.courseAudits = new ArrayDeque<>().with {
                it.add(true)
                it.add(false)
                it.add(false)
                return it
            }
            it.courseDates = [new Date(), new Date(), new Date()] as Set
            it.courseGrades = ['A' as char, 'B' as char, 'Z' as char, 'W' as char]
            it.initiationDate = LocalDate.of(2016, 1, 1)
            return it
        }
        list << new Graduate(name: 'def', age: 20, allowance: 250.0).with {
            it.subjects << 'math'
            it.subjects << 'english'
            it.subjects << 'history'
            it.grades << 4L
            it.grades << 5L
            it.grades << 5L
            it.subjectGrade = 'A'
            it.graduationDate = YearMonth.of(2020, 12)
            it.courseAudits = new ArrayDeque<>().with {
                it.add(false)
                it.add(true)
                it.add(false)
                return it
            }
            it.courseDates = [new Date(), new Date(), new Date()] as Set
            it.courseGrades = ['A' as char, 'B' as char, 'Z' as char, 'W' as char]
            it.course = new Course(name: 'Physics', teacher: new Teacher(name: 'Feynman', tenure: true))
            return it
        }
        list << new Graduate(name: 'aaa', age: 30, allowance: 350.0)
        Path path = new Path('/home/kabram/temp/dp/graduate.orc')
        try {
            handle.open(path).write(list).close()
        } finally {
            Files.delete(Paths.get('/home/kabram/temp/dp/graduate.orc'))
        }

    }
}
