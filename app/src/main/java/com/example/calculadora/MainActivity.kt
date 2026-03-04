package com.example.calculadora

import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.findNavController
import com.example.calculadora.ui.LaboratoriosActivity

class














MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return when (item.itemId) {
            R.id.menu_sueldo -> {
                navController.navigate(R.id.FirstFragment)
                true
            }
            R.id.menu_combustible -> {
                navController.navigate(R.id.SecondFragment)
                true
            }
            R.id.menu_orden_servicio -> {
                // Abrir la Activity de Gestion de Ordenes de Servicio
                val intent = Intent(this, OrdenServicioActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.menu_techaudit -> {
                // Abrir TechAudit 2.0
                val intent = Intent(this, LaboratoriosActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}