package com.charliesong.demo0327.step

import java.util.*

data class StepBean(var steps: Int, var date: Date) {
    fun isMonthFirst() = date.date == 1
    fun isToday(): Boolean {
        var same = false
        Calendar.getInstance().apply {
            same = this.get(Calendar.YEAR) == date.year
                    && this.get(Calendar.MONTH) == date.month
                    && this.get(Calendar.DAY_OF_MONTH) == date.date
        }
        return same
    }
    fun getDate():String{
        if(isMonthFirst()){
            return "${date.month+1}/${date.date}"
        }else{
            return "${date.date}"
        }
    }
}