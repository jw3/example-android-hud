package com.github.jw3.examplehud

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : FragmentActivity(), OnCamFragmentInteractionListener, OnMapFragmentInteractionListener {
    val sm = Machine(this)

    var cam0: CamView? = null
    var cam1: CamView? = null
    var map: MapView? = null
    var minimap: MapView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cam.visibility = View.INVISIBLE
        cam.setBackgroundColor(Color.GREEN)

        map = MapView.newInstance("map")
        minimap = MapView.newInstance("minimap")
        cam0 = CamView.newInstance("cam0")
        cam1 = CamView.newInstance("cam1")

        supportFragmentManager
            .beginTransaction()
            .disallowAddToBackStack()
            .replace(R.id.map, map)
            .replace(R.id.minimap0, minimap)
            .replace(R.id.minicam0, cam0)
            .replace(R.id.minicam1, cam1)
            .commit()


        minicam0.setOnClickListener {
            sm.call("cam0")
        }
        minicam1.setOnClickListener {
            sm.call("cam1")
        }
    }

    fun activateCam0() {
        println("activate cam0")
        val v = cam0?.view
        val cp = cam as ViewGroup
        minicam0.removeView(v)
        cp.addView(v)
        cp.visibility = View.VISIBLE
    }

    fun activateCam1() {
        println("activate cam1")
        val v = cam1?.view
        val cp = cam as ViewGroup
        minicam1.removeView(v)
        cp.addView(v)
        cp.visibility = View.VISIBLE

    }

    fun deactivateCam0() {
        println("deactivate cam0")
        val v = cam0?.view
        val cp = cam as ViewGroup
        cp.removeView(v)
        minicam0.addView(v)
        cp.visibility = View.INVISIBLE
    }

    fun deactivateCam1() {
        println("deactivate cam1")
        val v = cam1?.view
        val cp = cam as ViewGroup
        cp.removeView(v)
        minicam1.addView(v)
        cp.visibility = View.INVISIBLE
    }

    override fun onFragmentInteraction(f: CamView) {
        f.idStr()?.let { sm.call(it) }
    }

    override fun onFragmentInteraction(f: MapView) {
        f.idStr()?.let { sm.call(it) }
    }
}

interface OnCamFragmentInteractionListener {
    fun onFragmentInteraction(f: CamView)
}

interface OnMapFragmentInteractionListener {
    fun onFragmentInteraction(f: MapView)
}

interface StateT<out T> : (String) -> T
interface State : StateT<State>

class Machine(val ax: MainActivity) {
    var current: State = DefaultState()

    fun call(e: String) {
        current = current(e)
    }

    inner class DefaultState : State {
        override operator fun invoke(s: String): State {
            return when (s) {
                "cam0" -> {
                    ax.activateCam0()
                    ViewingCam0()
                }
                "cam1" -> {
                    ax.activateCam1()
                    ViewingCam1()
                }
                else -> this
            }
        }
    }

    inner class ViewingCam0 : State {
        override operator fun invoke(s: String): State {
            return when (s) {
                "cam0" -> {
                    ax.deactivateCam0()
                    DefaultState()
                }
                "cam1" -> {
                    ax.deactivateCam0()
                    ax.activateCam1()
                    ViewingCam1()
                }
                else -> this
            }
        }
    }

    inner class ViewingCam1 : State {
        override operator fun invoke(s: String): State {
            return when (s) {
                "cam0" -> {
                    ax.deactivateCam1()
                    ax.activateCam0()
                    ViewingCam0()
                }
                "cam1" -> {
                    ax.deactivateCam1()
                    DefaultState()
                }
                else -> this
            }
        }
    }
}
