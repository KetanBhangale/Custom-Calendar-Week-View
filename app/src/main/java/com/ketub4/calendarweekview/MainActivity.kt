package com.ketub4.calendarweekview

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import com.ketub4.calendarweekview.databinding.ActivityMainBinding
import java.time.LocalDate

class MainActivity : AppCompatActivity(), ICalendarAdapter {
    private var _binding: ActivityMainBinding ?=null
    val binding: ActivityMainBinding get() = _binding!!
    lateinit var calendarAdapter: CalendarAdapter
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding?.root)

        setupWeekCalendar()


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupWeekCalendar() {
        Log.i("selectedDate", "selectedDate=" + LocalDate.now())
        binding.calendarView.monthYearTV.text =
            CalendarUtils.monthYearFromDate(CalendarUtils.selectedDate)
        val days: ArrayList<LocalDate?> = CalendarUtils.daysInWeekArray(CalendarUtils.selectedDate)


        calendarAdapter = CalendarAdapter(days, this)

        binding.calendarView.calendarRecyclerView.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 7)
            hasFixedSize()
            adapter = calendarAdapter
        }

        binding.calendarView.previousWeek.setOnClickListener {
            val oldDate = CalendarUtils.selectedDate.minusWeeks(1)
            if (oldDate < LocalDate.now()) {
                Toast.makeText(
                    this@MainActivity,
                    "You can not select Previous Week.",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                CalendarUtils.selectedDate = oldDate
                setupWeekCalendar()
            }


        }
        binding.calendarView.nextWeek.setOnClickListener {

            CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1)
            setupWeekCalendar()
        }
        binding.calendarView.calenderIcon.setOnClickListener {
            val isVisibile = binding.calendarView.calendarMainView.visibility
            Log.i("isVisibile", "isVisibile=" + isVisibile)
            if (isVisibile == View.VISIBLE) {
                binding.calendarView.calendarMainView.visibility = View.GONE
                binding.DetailsCardView.visibility = View.GONE
            } else {
                binding.calendarView.calendarMainView.visibility = View.VISIBLE
                binding.DetailsCardView.visibility = View.VISIBLE
            }
        }

        setUI(CalendarUtils.selectedDate)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCalendarItemClicked(position: Int, localDate: LocalDate) {
        if (localDate < LocalDate.now()) {
            Toast.makeText(
                this@MainActivity,
                "You can not Select Older dates.",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            CalendarUtils.selectedDate = localDate
            setupWeekCalendar()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUI(localDate: LocalDate){
        Log.i("date","date="+localDate.dayOfMonth+"="+localDate.dayOfWeek
                +"="+localDate.dayOfYear+"="+localDate.month.toString())

        _binding?.monthName?.text = "Selected Month: ${localDate.month.name}"
        _binding?.date?.text = "Selected Date: ${CalendarUtils.formattedDate(localDate)}"
        _binding?.day?.text = "Selected Day: ${localDate.dayOfWeek}"

    }
}