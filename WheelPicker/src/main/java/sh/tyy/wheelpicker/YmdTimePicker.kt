package sh.tyy.wheelpicker

import android.content.Context
import sh.tyy.wheelpicker.core.WheelPickerActionSheet

class YmdTimePicker(context: Context) : WheelPickerActionSheet<YmdTimePickerView>(context) {
    init {
        setPickerView(YmdTimePickerView(context))
    }
}