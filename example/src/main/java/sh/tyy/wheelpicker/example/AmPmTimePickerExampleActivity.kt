package sh.tyy.wheelpicker.example

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import sh.tyy.wheelpicker.AmPmTimePicker
import sh.tyy.wheelpicker.AmPmTimePickerView
import java.text.SimpleDateFormat
import java.util.*

class AmPmTimePickerExampleActivity : AppCompatActivity(), PickerExample {

    private lateinit var amPmTimePickerView: AmPmTimePickerView
    override val circularCheckBox: CheckBox
        get() = findViewById(R.id.circular_check_box)
    override val vibrationFeedbackCheckBox: CheckBox
        get() = findViewById(R.id.vibration_feedback_check_box)
    override val selectedItemTextView: TextView
        get() = findViewById(R.id.selected_text_view)

    private val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_picker)
        title = "AM/PM Time"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        amPmTimePickerView = findViewById(R.id.am_pm_time_picker_view)
        vibrationFeedbackCheckBox.isChecked = amPmTimePickerView.isHapticFeedbackEnabled
        vibrationFeedbackCheckBox.setOnCheckedChangeListener { _, isChecked ->
            amPmTimePickerView.isHapticFeedbackEnabled = isChecked
        }

        circularCheckBox.setOnCheckedChangeListener { _, isChecked ->
            amPmTimePickerView.isCircular = isChecked
        }

        amPmTimePickerView.setWheelListener(object : AmPmTimePickerView.Listener {
            override fun didSelectData(isAm: Boolean, hour: Int, minute: Int) {
                calendar.set(Calendar.HOUR, hour)
                calendar.set(Calendar.MINUTE, minute)
                calendar.set(Calendar.AM_PM, if (isAm) Calendar.AM else Calendar.PM)
                selectedItemTextView.text = formatter.format(calendar.time)
            }
        })

        setupAmPmTimePicker()

        val actionSheetButton: Button = findViewById(R.id.action_sheet_button)
        actionSheetButton.setOnClickListener {
            val picker = AmPmTimePicker(this)
            picker.show(window)
            picker.pickerView?.hour = amPmTimePickerView.hour
            picker.pickerView?.minute = amPmTimePickerView.minute
            picker.pickerView?.isAm = amPmTimePickerView.isAm
            picker.setOnClickOkButtonListener {
                val pickerView = picker.pickerView ?: return@setOnClickOkButtonListener
                amPmTimePickerView.hour = pickerView.hour
                amPmTimePickerView.minute = pickerView.minute
                amPmTimePickerView.isAm = pickerView.isAm
                picker.hide()
            }
            picker.setOnDismissListener {
                Toast.makeText(this, "Action Sheet Dismiss", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupAmPmTimePicker() {
        calendar.time = Date()
        val currentHour = calendar.get(Calendar.HOUR)
        val currentMinute = calendar.get(Calendar.MINUTE)
        val currentAmPm = calendar.get(Calendar.AM_PM)

        amPmTimePickerView.hour = if (currentHour == 0) 12 else currentHour // Convert 0 to 12 for 12-hour format
        amPmTimePickerView.minute = currentMinute
        amPmTimePickerView.isAm = (currentAmPm == Calendar.AM)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}