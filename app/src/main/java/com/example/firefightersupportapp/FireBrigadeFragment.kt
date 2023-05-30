package com.example.firefightersupportapp

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.*
import java.util.concurrent.TimeUnit


class FireBrigadeFragment : Fragment() {

    private lateinit var view: View
    private lateinit var timer: TextView
    private lateinit var btn_end: Button

    private var handler: Handler? = null
    private var handlerDown: Handler? = null
    private var runnable: Runnable? = null
    private var runnableDown: Runnable? = null
    private var lastTime: Date = Date()
    private var lastMinPressure: Int = 0

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
    private lateinit var ff1_check4: EditText
    private lateinit var ff2_check4: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = inflater.inflate(R.layout.fragment_fire_brigade, container, false)
        timer = view.findViewById(R.id.Timer)
        btn_end = view.findViewById(R.id.btn_end1)

        setupButtonListener(R.id.btn_check1, R.id.ff1_check1, R.id.ff2_check1)
        setupButtonListener(R.id.btn_check2, R.id.ff1_check2, R.id.ff2_check2)
        setupButtonListener(R.id.btn_check3, R.id.ff1_check3, R.id.ff2_check3)
        setupButtonListener(R.id.btn_check4, R.id.ff1_check4, R.id.ff2_check4)
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

        btn_end.setOnClickListener {
            stopTimer()
            stopTimerDown()
        }

        return view
    }

    private fun setupButtonListener(
        buttonId: Int,
        ff1Id: Int,
        ff2Id: Int
    ) {
        val button = view.findViewById<Button>(buttonId)
        val ff1 = view.findViewById<EditText>(ff1Id)
        val ff2 = view.findViewById<EditText>(ff2Id)

        button.setOnClickListener {
            var ff1_val = ff1.text.toString().toIntOrNull()
            var ff2_val = ff2.text.toString().toIntOrNull()
            if(ff1_val == null || ff2_val == null){
                showAlertDialog("Uwaga", "Wartości ciśnienia strażaków nie mogą pozostać puste")
            } else {
                if(ff1_val<0 || ff1_val>350 || ff2_val<0 || ff2_val>350){
                    showAlertDialog("Uwaga", "Wartości ciśnienia strażaków muszą być w zakresie [0:350]")
                } else {
                    val minPressure = getMinPressure(ff1_val, ff2_val)
                    val parentView = button.parent as ViewGroup
                    val buttonIndex = parentView.indexOfChild(button)
                    parentView.removeView(button)
                    val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                    val currentDate = Date()
                    val formattedDate = dateFormat.format(currentDate)
                    val textView = TextView(requireActivity())
                    textView.text = formattedDate
                    textView.gravity = Gravity.CENTER
                    parentView.addView(textView, buttonIndex, button.layoutParams)
                    if (button.id == R.id.btn_check1) {
                        startTimer()
                        lastTime = Date()
                        lastMinPressure = minPressure
                        stopTimerDown()
                        startTimerDown((((minPressure * 6) / 50) * 60 * 1000).toLong())
                    } else {
                        val remainingTime = updateTimeToEscape(lastMinPressure, minPressure, lastTime, Date())
                        lastTime = Date()
                        lastMinPressure = minPressure
                        stopTimerDown()
                        startTimerDown(remainingTime)
                    }
                }
            }

        }
    }
    private fun showAlertDialog(title: String, message: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
    private fun getMinPressure(p1: Int, p2: Int): Int {
        var min_pressure = 0

        if (p1 > p2) {
            println("p1 is greater than or similar to p2")
            min_pressure = p2;
        } else {
            println("p2 is greater than or similar to p1")
            min_pressure = p1;
        }

        if ((min_pressure - 50) < 0) {
            showAlertDialog("Uwaga", "Przekroczono bezpieczną granicę ciśnienia")
        } else {
            println("Min pressure = $min_pressure")
        }
        return min_pressure
    }



    private fun updateTimeToEscape(
        lastMinPressure: Int,
        currentMinPressure: Int,
        lastTime: Date,
        currentTime: Date
    ): Long {
        var minutes = TimeUnit.MILLISECONDS.toMinutes(currentTime.time - lastTime.time)
        if (minutes < 1) {
            minutes = 1
        }
        println((currentMinPressure * 6) / ((lastMinPressure - currentMinPressure) * 6 / minutes))
        val timeToEscape =
            (currentMinPressure * 6) / ((lastMinPressure - currentMinPressure) * 6 / minutes)
        return timeToEscape * 60 * 1000
    }


    private fun startTimer() {
        var elapsedTime: Int
        val startTime = System.currentTimeMillis()
        handler = Handler()
        runnable = object : Runnable {
            override fun run() {
                elapsedTime = (System.currentTimeMillis() - startTime).toInt()

                val hours = (elapsedTime / 1000) / 3600
                val minutes = (elapsedTime / 1000) / 60
                val seconds = (elapsedTime / 1000) % 60

                timer = view.findViewById(R.id.Timer)
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
        var elapsedTime: Int
        val startTime = System.currentTimeMillis()
        handlerDown = Handler()
        runnableDown = object : Runnable {
            override fun run() {
                elapsedTime = (remainingTime + startTime - System.currentTimeMillis() + 1000).toInt()

                val hours = (elapsedTime / 1000) / 3600
                val minutes = (elapsedTime / 1000) / 60
                val seconds = (elapsedTime / 1000) % 60

                timer = view.findViewById(R.id.remaining_time)
                timer.text = String.format(
                    Locale.getDefault(),
                    "%02dh %02dm %02ds",
                    hours,
                    minutes,
                    seconds
                )

                if (hours <= 0 && minutes <= 0 && seconds <= 0) {
                    stopTimer()
                    stopTimerDown()
                } else {
                    handlerDown?.postDelayed(this, 10)
                }
            }
        }

        handlerDown?.postDelayed(runnableDown as Runnable, 10)
    }

    private fun stopTimerDown() {
        runnableDown?.let { handlerDown?.removeCallbacks(it) }
    }
}