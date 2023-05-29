package com.example.firefightersupportapp

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.firefightersupportapp.databinding.ActivityMainBinding
import com.example.firefightersupportapp.databinding.FragmentFireBrigadeBinding
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.*
import kotlin.math.ceil


class FireBrigadeFragment : Fragment() {

    private lateinit var binding: FragmentFireBrigadeBinding

    private lateinit var view: View
    private lateinit var btn_check1: Button
    private lateinit var ff1_check1: EditText
    private lateinit var ff2_check1: EditText
    private lateinit var btn_check2: Button
    private lateinit var ff1_check2: EditText
    private lateinit var ff2_check2: EditText
    private lateinit var btn_check3: Button
    private lateinit var ff1_check3: EditText
    private lateinit var ff2_check3: EditText
    private lateinit var btn_check4: Button
    private lateinit var btn_end: Button
    private lateinit var ff1_check4: EditText
    private lateinit var ff2_check4: EditText
    private lateinit var timer: TextView

    private var startTime: Long = 0
    private var handler: Handler? = null
    private var runnable: Runnable? = null
    private var remainingTime: Long = 0
    private var lastTime: Date = Date()
    private var lastMinPressure: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = inflater.inflate(R.layout.fragment_fire_brigade, container, false)
        ff1_check1 = view.findViewById(R.id.ff1_check1)
        ff2_check1 = view.findViewById(R.id.ff2_check1)
        btn_check1 = view.findViewById(R.id.btn_check1)
        ff1_check2 = view.findViewById(R.id.ff1_check2)
        ff2_check2 = view.findViewById(R.id.ff2_check2)
        btn_check2 = view.findViewById(R.id.btn_check2)
        ff1_check3 = view.findViewById(R.id.ff1_check3)
        ff2_check3 = view.findViewById(R.id.ff2_check3)
        btn_check3 = view.findViewById(R.id.btn_check3)
        ff1_check4 = view.findViewById(R.id.ff1_check4)
        ff2_check4 = view.findViewById(R.id.ff2_check4)
        btn_check4 = view.findViewById(R.id.btn_check4)
        btn_end = view.findViewById(R.id.btn_end1)

        val f1_spinner = view.findViewById<Spinner>(R.id.firefighter1)
        val f2_spinner = view.findViewById<Spinner>(R.id.firefighter2)
        val options = DBHelper(requireContext(), null).getNames()
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            options
        )
        f1_spinner.adapter = adapter
        f1_spinner.setSelection(options.indexOf(""))
        f2_spinner.adapter = adapter
        f2_spinner.setSelection(options.indexOf(""))

        btn_check1.setOnClickListener {
            val minPressure = getMinPressure(ff1_check1, ff2_check1)
            val parentView = btn_check1.parent as ViewGroup
            val buttonIndex = parentView.indexOfChild(btn_check1)
            parentView.removeView(btn_check1)
            val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val currentDate = Date()
            val formattedDate = dateFormat.format(currentDate)
            val textView = TextView(requireActivity())
            textView.text = formattedDate
            textView.gravity = Gravity.CENTER
            parentView.addView(textView, buttonIndex, btn_check1.layoutParams)
            startTimer()
            remainingTime = (((minPressure*6)/50)*60*1000).toLong()
//            println(remainingTime)
            lastTime = Date()
            lastMinPressure = minPressure
            startTimerDown(remainingTime)
        }

        btn_check2.setOnClickListener {
            val minPressure = getMinPressure(ff1_check2, ff2_check2)
            val parentView = btn_check2.parent as ViewGroup
            val buttonIndex = parentView.indexOfChild(btn_check2)
            parentView.removeView(btn_check2)
            val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val currentDate = Date()
            val formattedDate = dateFormat.format(currentDate)
            val textView = TextView(requireActivity())
            textView.text = formattedDate
            textView.gravity = Gravity.CENTER
            parentView.addView(textView, buttonIndex, btn_check2.layoutParams)
            updateTimeToEscape(lastMinPressure,minPressure,lastTime,Date())
            lastTime = Date()
            lastMinPressure = minPressure
        }

        btn_check3.setOnClickListener {
            val minPressure = getMinPressure(ff1_check3, ff2_check3)
            val parentView = btn_check3.parent as ViewGroup
            val buttonIndex = parentView.indexOfChild(btn_check3)
            parentView.removeView(btn_check3)
            val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val currentDate = Date()
            val formattedDate = dateFormat.format(currentDate)
            val textView = TextView(requireActivity())
            textView.text = formattedDate
            textView.gravity = Gravity.CENTER
            parentView.addView(textView, buttonIndex, btn_check3.layoutParams)
            updateTimeToEscape(lastMinPressure,minPressure,lastTime,Date())
            lastTime = Date()
            lastMinPressure = minPressure
        }
        btn_check4.setOnClickListener {
            val minPressure = getMinPressure(ff1_check4, ff2_check4)
            val parentView = btn_check4.parent as ViewGroup
            val buttonIndex = parentView.indexOfChild(btn_check4)
            parentView.removeView(btn_check4)
            val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val currentDate = Date()
            val formattedDate = dateFormat.format(currentDate)
            val textView = TextView(requireActivity())
            textView.text = formattedDate
            textView.gravity = Gravity.CENTER
            parentView.addView(textView, buttonIndex, btn_check4.layoutParams)
            updateTimeToEscape(lastMinPressure,minPressure,lastTime,Date())
            lastTime = Date()
            lastMinPressure = minPressure
        }
        btn_end.setOnClickListener {
            stopTimer()
            stopTimerDown()
        }

        return view
    }

    private fun getMinPressure(p1:EditText,p2:EditText): Int {
        val p1_val = p1.text.toString().toIntOrNull()
        val p2_val = p2.text.toString().toIntOrNull()
        var min_pressure = 0
        if (p1_val != null && p2_val != null) {
            if (p1_val > p2_val) {
                println("p1 is greater than or similar to p2")
                min_pressure=p2_val;
            } else {
                println("p2 is greater than or similar to p1")
                min_pressure=p1_val;
            }
        } else {
            if(p1_val == null && p2_val == null){
                println("ERRROR")
            }else{
                if(p1_val == null && p2_val != null) {
                    println(p2_val)
                    min_pressure=p2_val

                }else if(p1_val != null){
                    println(p1_val)
                    min_pressure=p1_val;
                }
            }
        }
        if((min_pressure-50)<0){
            print("No time to go inside smoke")
        } else{
            println("Min pressure = "+ min_pressure)
        }
        return min_pressure
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateTimeToEscape(lastMinPressure:Int, currentMinPressure: Int, lastTime: Date, currentTime: Date){
        var minutes = Duration.between(lastTime.toInstant(), currentTime.toInstant()).toMinutes()
        if (minutes < 1){
            minutes = 1
        }
        println((currentMinPressure*6)/((lastMinPressure-currentMinPressure)*6/minutes))
        var timeToEscape = (currentMinPressure*6)/((lastMinPressure-currentMinPressure)*6/minutes)
        remainingTime = timeToEscape*60*1000
        startTimerDown(remainingTime)
    }
    private fun startTimer() {
        startTime = System.currentTimeMillis()
        handler = Handler()
        runnable = object : Runnable {
            override fun run() {
                val elapsedTime = System.currentTimeMillis() - startTime
                val hours = (elapsedTime/1000) /3600
                val minutes = (elapsedTime / 1000) / 60
                val seconds = (elapsedTime / 1000) % 60


                timer = view.findViewById<TextView>(R.id.Timer)
                timer.text = String.format(
                    Locale.getDefault(),
                    "%02d:%02d:%02d",
                    hours,
                    minutes,
                    seconds
                )

                handler?.postDelayed(this, 10)
            }
        }

        handler?.postDelayed(runnable as Runnable, 10)
    }

    private fun stopTimer() {
        runnable?.let { handler?.removeCallbacks(it) }
    }
    private fun startTimerDown(remainingTime: Long) {
        startTime = System.currentTimeMillis()
        handler = Handler()
        runnable = object : Runnable {
            override fun run() {
                val elapsedTime = remainingTime + startTime - System.currentTimeMillis() + 1000
                val hours = (elapsedTime/1000) /3600
                val minutes = (elapsedTime / 1000) / 60
                val seconds = (elapsedTime / 1000) % 60


                timer = view.findViewById<TextView>(R.id.remaining_time)
                timer.text = String.format(
                    Locale.getDefault(),
                    "%02dh %02dm %02ds",
                    hours,
                    minutes,
                    seconds
                )

                handler?.postDelayed(this, 10)
            }
        }

        handler?.postDelayed(runnable as Runnable, 10)
    }

    private fun stopTimerDown() {
        runnable?.let { handler?.removeCallbacks(it) }
    }
}