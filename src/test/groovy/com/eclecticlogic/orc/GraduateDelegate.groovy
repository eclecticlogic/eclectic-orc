package com.eclecticlogic.orc

class GraduateDelegate {

    Graduate delegate

    GraduateDelegate(Graduate delegate) {
        this.delegate = delegate
    }


    String major() {
        return "Major " + delegate.major + " " + (delegate.course == null ? "null" : delegate.course.name);
    }
}
