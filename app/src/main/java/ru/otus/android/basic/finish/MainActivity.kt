package ru.otus.android.basic.finish

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import ru.otus.android.basic.service.databinding.ActivityMainBinding
import ru.otus.android.basic.finish.services.BackgroundService
import ru.otus.android.basic.finish.services.BoundService
import ru.otus.android.basic.finish.services.BoundService.UploadBinder
import ru.otus.android.basic.finish.services.ForegroundService
import ru.otus.android.basic.utils.runWithPostNotificationPermission

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val connection: ServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            (service as UploadBinder).subscribeToProgress { progress ->
                runOnUiThread {
                    binding.progress.text = "$progress%"
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.startBackground.setOnClickListener {
            Intent(this, BackgroundService::class.java).also { intent ->
                startService(intent)
            }
        }

        binding.startForeground.setOnClickListener {
            runWithPostNotificationPermission {
                Intent(this, ForegroundService::class.java).also { intent ->
                    startForegroundService(intent)
                }
            }
        }

        binding.startBound.setOnClickListener {
            Intent(this, BoundService::class.java).also { intent ->
                startService(intent)
            }
        }

        binding.tryIPC.setOnClickListener {
            Intent(this, PingPongActivity::class.java).also { intent ->
                startActivity(intent)
            }
        }
    }

    override fun onStart() {
        super.onStart()

        Intent(this, BoundService::class.java).also { intent ->
            bindService(intent, connection, BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
    }
}
