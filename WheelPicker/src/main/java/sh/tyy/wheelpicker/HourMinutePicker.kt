package sh.tyy.wheelpicker

import android.content.Context
import sh.tyy.wheelpicker.core.WheelPickerActionSheet

class HourMinutePicker(context: Context) : WheelPickerActionSheet<HourMinutePickerView>(context) {
    init {
        setPickerView(HourMinutePickerView(context))
    }
}