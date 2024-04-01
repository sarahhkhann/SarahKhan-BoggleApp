package com.example.sarahkhan_boggleapp

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

class ScoreFragment : Fragment(), SensorEventListener {
    private lateinit var scoreTextView: TextView
    private var listener: OnNewGameRequestedListener? = null
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var lastUpdate: Long = 0
    private var last_x: Float = 0.0f
    private var last_y: Float = 0.0f
    private var last_z: Float = 0.0f
    private val shakeThreshold: Int = 500

    interface OnNewGameRequestedListener {
        fun onNewGameRequested()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_score, container, false)

        scoreTextView = view.findViewById(R.id.scoreTextView)

        view.findViewById<Button>(R.id.newGameButton).setOnClickListener {
            listener?.onNewGameRequested()
        }

        sensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        return view
    }

    fun updateScore(score: Int) {
        Log.d("ScoreFragment", "score being written: $score")
        scoreTextView.text = "Score: $score"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnNewGameRequestedListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnNewGameRequestedListener")
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                val x = it.values[0]
                val y = it.values[1]
                val z = it.values[2]

                val curTime = System.currentTimeMillis()
                if ((curTime - lastUpdate) > 100) {
                    val diffTime = (curTime - lastUpdate)
                    lastUpdate = curTime

                    val speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000

                    if (speed > shakeThreshold) {
                        listener?.onNewGameRequested()
                    }

                    last_x = x
                    last_y = y
                    last_z = z
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Can be left empty
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}
