package com.example.weatherapp.reminder.view

import android.annotation.SuppressLint
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.BraodcastNoti
import com.example.weatherapp.BraodcastNoti.Companion.NOTIFICATION
import com.example.weatherapp.BraodcastNoti.Companion.NOTIFICATION_ID
import com.example.weatherapp.R
import com.example.weatherapp.data.model.ReminderModel
import com.example.weatherapp.databinding.FragmentReminderBinding
import com.example.weatherapp.reminder.viewModel.ReminderViewModel
import kotlinx.coroutines.launch
import java.util.*


class ReminderFragment : Fragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    lateinit var txtFromDate: TextView
    lateinit var txtToDate: TextView

    private var fromDate = ""
    private var count = 0


    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent

    private lateinit var binding: FragmentReminderBinding
    var day = 0
    var month: Int = 0
    var year: Int = 0
    var hour: Int = 0
    var minute: Int = 0
    var myDay = 0
    var myMonth: Int = 0
    var myYear: Int = 0
    var myHour: Int = 0
    var myMinute: Int = 0

    var timeInMille1 = 0L
    var timeInMille2 = 0L

    private val viewModel: ReminderViewModel by viewModels {
        ReminderModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_reminder, container, false)
        binding = FragmentReminderBinding.bind(view)


        binding.alarmRecycler.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )
        binding.alarmRecycler.setHasFixedSize(true)


        viewModel.initDatabase()

        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAlarm.setOnClickListener {
            showAlarmDialog()
        }

        getAllRemData()

    }


    private fun getAllRemData() {
        viewModel.getRem().observe(requireActivity()) {
            if (it.isNotEmpty()) {
                val lambdaDelete = { id: Int -> deleteFromFav(id) }
                val favAdapter = ReminderAdapter(lambdaDelete, it)
                binding.alarmRecycler.adapter = favAdapter
                binding.noLayout.visibility = View.GONE
                binding.alarmRecycler.visibility = View.VISIBLE
            } else {
                binding.noLayout.visibility = View.VISIBLE
                binding.alarmRecycler.visibility = View.GONE
            }
        }
    }

    private fun deleteFromFav(id: Int) {
        lifecycleScope.launch {
            viewModel
            viewModel.deleteFromRem(id)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showAlarmDialog() {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.alarm_layout, null)
        txtFromDate = view.findViewById(R.id.txtFromDate)

        txtToDate = view.findViewById(R.id.txtToDate)
        val btnOk: Button = view.findViewById(R.id.btnSetAlarm)


        val dialog = Dialog(requireContext(), R.style.Theme_Dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        txtFromDate.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            day = calendar.get(Calendar.DAY_OF_MONTH)
            month = calendar.get(Calendar.MONTH)
            year = calendar.get(Calendar.YEAR)
            val datePickerDialog = DatePickerDialog(requireContext(), this, year, month, day)
            datePickerDialog.show()
            fromDate = "1"
        }

        txtToDate.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            day = calendar.get(Calendar.DAY_OF_MONTH)
            month = calendar.get(Calendar.MONTH)
            year = calendar.get(Calendar.YEAR)
            val datePickerDialog = DatePickerDialog(requireContext(), this, year, month, day)
            datePickerDialog.show()
            fromDate = "2"
        }

        btnOk.setOnClickListener {
            if (count > 1) {
                val model = ReminderModel(
                    0,
                    txtFromDate.text.toString(),
                    txtToDate.text.toString(),
                    timeInMille1,
                    timeInMille2
                )
                //scheduleNotification(getNotification("weather")!!, timeInMille1)
                methoud()
                viewModel.insertRem(model)
                dialog.dismiss()
            }
        }

        dialog.setContentView(view)
        dialog.show()
    }

    private fun methoud() {
        alarmMgr = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(context, BraodcastNoti::class.java).let { intent ->
            PendingIntent.getBroadcast(context, 0, intent, 0)
        }

// Set the alarm to start at 8:30 a.m.
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, myHour)
            set(Calendar.MINUTE, myMinute-1)
        }

// setRepeating() lets you specify a precise custom interval--in this case,
// 20 minutes.
        alarmMgr?.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            1000 * 60 * 20,
            alarmIntent)
    }

    private fun scheduleNotification(notification: Notification, delay: Long) {
        val notificationIntent = Intent(requireContext(), BraodcastNoti::class.java)
        notificationIntent.putExtra(BraodcastNoti.NOTIFICATION_ID, 1)
        notificationIntent.putExtra(BraodcastNoti.NOTIFICATION, notification)
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager =
            (requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager?)!!
        alarmManager[AlarmManager.ELAPSED_REALTIME_WAKEUP, delay] = pendingIntent
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getNotification(content: String): Notification? {
        val builder: Notification.Builder =
            Notification.Builder(requireContext(), "123")
        builder.setContentTitle("Scheduled Notification")
        builder.setContentText(content)
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
        builder.setAutoCancel(true)
        builder.setChannelId("123")
        return builder.build()
    }

    private fun setDataToalarm() {
        val notifyIntent = Intent(requireContext(), BroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            1,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis(),
            (1000 * 60 * 60 * 24).toLong(),
            pendingIntent
        )
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        myDay = day
        myYear = year
        myMonth = month
        val calendar: Calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR)
        minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(
            requireContext(), this, hour, minute,
            DateFormat.is24HourFormat(requireContext())
        )
        timePickerDialog.show()
    }

    @SuppressLint("SetTextI18n")
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        myHour = hourOfDay
        myMinute = minute

        if (fromDate == "1") {
            count++
            txtFromDate.text = "$myYear/$myMonth/$myDay  -  $myHour:$myMinute"
            val calender = Calendar.getInstance()
            calender.set(myYear, myMonth, myDay, myHour, myMinute)
            timeInMille1 = calender.timeInMillis
        } else {
            count++
            txtToDate.text = "$myYear/$myMonth/$myDay  -  $myHour:$myMinute"
            val calender = Calendar.getInstance()
            calender.set(myYear, myMonth, myDay, myHour, myMinute)
            timeInMille2 = calender.timeInMillis
        }

    }


}