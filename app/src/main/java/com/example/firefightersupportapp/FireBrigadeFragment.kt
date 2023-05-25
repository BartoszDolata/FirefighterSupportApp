package com.example.firefightersupportapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TableRow
import androidx.fragment.app.Fragment


class FireBrigadeFragment : Fragment() {

    private lateinit var view: View
    private lateinit var table: TableLayout
    private lateinit var editText1_3: EditText
    private lateinit var editText2_3: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = inflater.inflate(R.layout.fragment_fire_brigade, container, false)
        table = view.findViewById(R.id.table)
        editText1_3 = view.findViewById(R.id.ff1_check3)
        editText2_3 = view.findViewById(R.id.ff2_check3)

        editText1_3.addTextChangedListener(MyTextWatcher())

        editText2_3.addTextChangedListener(MyTextWatcher())

        return view
    }

    inner class MyTextWatcher : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (p0!!.isNotEmpty()) {
                //dodaj kolumne w 2 i 3 rzedzie
                val row2 = table.getChildAt(1) as TableRow
                val row3 = table.getChildAt(2) as TableRow

                val editText1 = EditText(this@FireBrigadeFragment.context)
                editText1.setHint(R.string.hint)
                editText1.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                editText1.addTextChangedListener(MyTextWatcher())

                val editText2 = EditText(this@FireBrigadeFragment.context)
                editText2.setHint(R.string.hint)
                editText2.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                editText2.addTextChangedListener(MyTextWatcher())

                row2.addView(editText1)
                row3.addView(editText2)
            }
        }
    }
}