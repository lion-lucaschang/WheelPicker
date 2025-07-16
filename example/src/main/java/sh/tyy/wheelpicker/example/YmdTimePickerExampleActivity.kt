package sh.tyy.wheelpicker.example

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import sh.tyy.wheelpicker.YmdTimePicker
import sh.tyy.wheelpicker.YmdTimePickerView
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class YmdTimePickerExampleActivity : AppCompatActivity(), PickerExample {

    private lateinit var ymdTimePickerView: YmdTimePickerView
    override val circularCheckBox: CheckBox
        get() = findViewById(R.id.circular_check_box)
    override val vibrationFeedbackCheckBox: CheckBox
        get() = findViewById(R.id.vibration_feedback_check_box)
    override val selectedItemTextView: TextView
        get() = findViewById(R.id.selected_text_view)

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ymd_time_picker)
        title = "YMD Time Picker"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        ymdTimePickerView = findViewById(R.id.ymd_time_picker_view)
        ymdTimePickerView.startDate = LocalDate.of(2022, 1, 1)
        ymdTimePickerView.endDate = LocalDate.of(2025, 2, 28)
        vibrationFeedbackCheckBox.isChecked = ymdTimePickerView.isHapticFeedbackEnabled
        vibrationFeedbackCheckBox.setOnCheckedChangeListener { _, isChecked ->
            ymdTimePickerView.isHapticFeedbackEnabled = isChecked
        }

        circularCheckBox.setOnCheckedChangeListener { _, isChecked ->
            ymdTimePickerView.isCircular = isChecked
        }

        ymdTimePickerView.setWheelListener(object : YmdTimePickerView.Listener {
            override fun didSelectData(date: LocalDate, hour: Int, minute: Int) {
                selectedItemTextView.text =
                    "${formatter.format(date)} %02d:%02d".format(hour, minute)
            }
        })

        setupYmdTimePicker()

        val actionSheetButton: Button = findViewById(R.id.action_sheet_button)
        actionSheetButton.setOnClickListener {
            val picker = YmdTimePicker(this)
            picker.show(window)
            picker.pickerView?.date = ymdTimePickerView.date
            picker.pickerView?.hour = ymdTimePickerView.hour
            picker.pickerView?.minute = ymdTimePickerView.minute
            picker.setOnClickOkButtonListener {
                val pickerView = picker.pickerView ?: return@setOnClickOkButtonListener
                ymdTimePickerView.hour = pickerView.hour
                ymdTimePickerView.minute = pickerView.minute
                ymdTimePickerView.date = pickerView.date
                picker.hide()
            }
            picker.setOnDismissListener {
                Toast.makeText(this, "Action Sheet Dismiss", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupYmdTimePicker() {
        val currentDate = LocalDate.now()
        ymdTimePickerView.date = currentDate
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
