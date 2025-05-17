package epi.gl4.tp5GESG2

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import epi.gl4.tp5GESG2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, SensorEventListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private lateinit var gyroscope: Sensor
    private lateinit var magnetometer: Sensor
    private lateinit var proximity: Sensor
    private lateinit var light: Sensor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        // Initialisation des vues
        drawerLayout = binding.drawerLayout
        navView = binding.navView

        // Configuration du FAB
        binding.appBarMain.fab.setOnClickListener { view ->
            showSnackbar(view)
        }

        // Initialisation des capteurs
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)!!
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)!!
        proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)!!
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)!!

        setupNavigation()
    }

    private fun setupNavigation() {
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow),
            drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_sen -> {
                sensorManager.unregisterListener(this)
                showSnackbar(binding.root, "Affichage de tous les capteurs disponibles")
            }
            R.id.nav_Acc -> {

                sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
                showSnackbar(binding.root, "Accéléromètre activé")
            }
            R.id.nav_gyr -> {
                sensorManager.unregisterListener(this,accelerometer)

                sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL)
                showSnackbar(binding.root, "Gyroscope activé")
            }
            R.id.nav_Mag -> {
                sensorManager.unregisterListener(this,gyroscope)

                sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL)
                showSnackbar(binding.root, "Magnétomètre activé")
            }
            R.id.nav_pro -> {
                sensorManager.unregisterListener(this,magnetometer)

                sensorManager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_NORMAL)
                showSnackbar(binding.root, "Capteur de proximité activé")
            }
            R.id.nav_pho -> {
                sensorManager.unregisterListener(this,proximity)

                sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL)
                showSnackbar(binding.root, "Photomètre activé")
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    // Implémentation de SensorEventListener
    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
val x=event.values[0]
 val y=event.values[1]
 val z=event.values[2]
                Toast.makeText(
                    this,
                    "x=${x} y=${y} z=${z}",  Toast.LENGTH_SHORT).show()            }
            Sensor.TYPE_GYROSCOPE -> {
                Toast.makeText(
                    this,
                    "Gyroscope - x=%.2f y=%.2f z=%.2f".format(
                        event.values[0],
                        event.values[1],
                        event.values[2]
                    ),
                    Toast.LENGTH_SHORT
                ).show()

            }
            Sensor.TYPE_MAGNETIC_FIELD -> {
                Toast.makeText(
                    this,
                    "Magnetometre - x=%.2f y=%.2f z=%.2f".format(
                        event.values[0],
                        event.values[1],
                        event.values[2]
                    ),
                    Toast.LENGTH_SHORT
                ).show()
            }
            Sensor.TYPE_PROXIMITY -> {
                Toast.makeText(
                    this,
                    "Proximity - x=%.2f y=%.2f z=%.2f".format(
                        event.values[0],
                        event.values[1],
                        event.values[2]
                    ),
                    Toast.LENGTH_SHORT
                ).show()
            }
            Sensor.TYPE_LIGHT -> {
                Toast.makeText(
                    this,
                    "Photometer - x=%.2f y=%.2f z=%.2f".format(
                        event.values[0]+1,
                        event.values[1],
                        event.values[2]
                    ),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Gestion des changements de précision
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onResume() {
        super.onResume()
        // Réactiver les capteurs si nécessaire
    }

    private fun showSnackbar(view: View, message: String = "Replace with your own action") {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .setAnchorView(R.id.fab)
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}