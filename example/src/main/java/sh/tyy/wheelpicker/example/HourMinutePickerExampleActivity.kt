package sh.tyy.wheelpicker.example

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import sh.tyy.wheelpicker.HourMinutePicker
import sh.tyy.wheelpicker.HourMinutePickerView
import java.text.SimpleDateFormat
import java.util.*

class HourMinutePickerExampleActivity : AppCompatActivity(), PickerExample {

    private lateinit var hourMinutePickerView: HourMinutePickerView
    override val circularCheckBox: CheckBox
        get() = findViewById(R.id.circular_check_box)
    override val vibrationFeedbackCheckBox: CheckBox
        get() = findViewById(R.id.vibration_feedback_check_box)
    override val selectedItemTextView: TextView
        get() = findViewById(R.id.selected_text_view)

    private val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hour_minute_picker)
        title = "Hour Minute"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        hourMinutePickerView = findViewById(R.id.hour_minute_picker_view)
        vibrationFeedbackCheckBox.isChecked = hourMinutePickerView.isHapticFeedbackEnabled
        vibrationFeedbackCheckBox.setOnCheckedChangeListener { _, isChecked ->
            hourMinutePickerView.isHapticFeedbackEnabled = isChecked
        }

        circularCheckBox.setOnCheckedChangeListener { _, isChecked ->
            hourMinutePickerView.isCircular = isChecked
        }

        hourMinutePickerView.setWheelListener(object : HourMinutePickerView.Listener {
            override fun didSelectData(hour: Int, minute: Int) {
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                selectedItemTextView.text = formatter.format(calendar.time)
            }
        })

        setupHourMinutePicker()

        val actionSheetButton: Button = findViewById(R.id.action_sheet_button)
        actionSheetButton.setOnClickListener {
            val picker = HourMinutePicker(this)
            picker.title = "請選擇時、分"
            picker.show(window)
            picker.pickerView?.hour = hourMinutePickerView.hour
            picker.pickerView?.minute = hourMinutePickerView.minute
            picker.setOnClickOkButtonListener {
                val pickerView = picker.pickerView ?: return@setOnClickOkButtonListener
                hourMinutePickerView.hour = pickerView.hour
                hourMinutePickerView.minute = pickerView.minute
                picker.hide()
            }
            picker.setOnDismissListener {
                Toast.makeText(this, "Action Sheet Dismiss", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupHourMinutePicker() {
        calendar.time = Date()
        hourMinutePickerView.hour = calendar.get(Calendar.HOUR_OF_DAY)
        hourMinutePickerView.minute = calendar.get(Calendar.MINUTE)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
