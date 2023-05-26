package com.example.firefightersupportapp

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.firefightersupportapp.databinding.FragmentFireBrigadeBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil


class FireBrigadeFragment : Fragment() {

    private lateinit var view: View
    private lateinit var table: TableLayout
    private lateinit var btn_check1: Button
    private lateinit var ff1_check1: EditText
    private lateinit var ff2_check1: EditText
    private lateinit var btn_check2: Button
    private lateinit var ff1_check2: EditText
    private lateinit var ff2_check3: EditText
    private lateinit var btn_check3: Button
    private lateinit var ff1_check4: EditText
    private lateinit var ff2_check4: EditText
//    private lateinit var addButton: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = inflater.inflate(R.layout.fragment_fire_brigade, container, false)
        table = view.findViewById(R.id.table)
//        addButton = view.findViewById(R.id.btn_add)
//        addButton.setOnClickListener { addColumn() }
        ff1_check1 = view.findViewById(R.id.ff1_check1)
        ff2_check1 = view.findViewById(R.id.ff2_check1)
        btn_check1 = view.findViewById(R.id.btn_check1)

        btn_check1.setOnClickListener {
            setTimeToEscape(ff1_check1, ff2_check1)
            val parentView = btn_check1.parent as ViewGroup
            val buttonIndex = parentView.indexOfChild(btn_check1)
            parentView.removeView(btn_check1)
            val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val currentDate = Date()
            val formattedDate = dateFormat.format(currentDate)
            // Tworzenie nowego elementu TextView
            val textView = TextView(requireActivity())
            textView.text = formattedDate

            textView.gravity = Gravity.CENTER // opcjonalne: ustawienie wyśrodkowania tekstu

            // Dodanie TextView do tego samego miejsca, w którym był przycisk
            parentView.addView(textView, buttonIndex, btn_check1.layoutParams)
        }
        return view
    }

//    private fun addColumn() {
//        Toast.makeText(context,"this is toast message",Toast.LENGTH_SHORT).show()
//
//        val view = table.getChildAt(1)
//        if (view is TableRow) {
//            Toast.makeText(context,"view is row",Toast.LENGTH_SHORT).show()
//
//            val editText = EditText(this@FireBrigadeFragment.context)
//            editText.setHint(R.string.hint)
//            editText.layoutParams = ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT
//            )
//            view.addView(editText)
//        }
//    }

//    inner class MyTextWatcher : TextWatcher {
//        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
//
//        override fun afterTextChanged(p0: Editable?) {}
//
//        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//            if (p0!!.isNotEmpty()) {
//                //dodaj kolumne w 2 i 3 rzedzie
//                val row2 = table.getChildAt(1) as TableRow
//                val row3 = table.getChildAt(2) as TableRow
//
//                val editText1 = EditText(this@FireBrigadeFragment.context)
//                editText1.setHint(R.string.hint)
//                editText1.layoutParams = ViewGroup.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.MATCH_PARENT
//                )
//                editText1.addTextChangedListener(MyTextWatcher())
//
//                val editText2 = EditText(this@FireBrigadeFragment.context)
//                editText2.setHint(R.string.hint)
//                editText2.layoutParams = ViewGroup.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.MATCH_PARENT
//                )
//                editText2.addTextChangedListener(MyTextWatcher())
//
//                row2.addView(editText1)
//                row3.addView(editText2)
//            }
//        }
//    }
private fun setTimeToEscape(p1:EditText,p2:EditText){
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
        println("Time to go inside = "+ ceil(((min_pressure-50) *6/50).toDouble()))
    }



    }
    private fun changeTimeToEscape(p1:EditText,p2:EditText){

    }
}