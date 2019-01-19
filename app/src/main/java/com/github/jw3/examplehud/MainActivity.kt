package com.github.jw3.examplehud

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : FragmentActivity(), OnCamFragmentInteractionListener, OnMapFragmentInteractionListener {
    val map = HashMap<String, Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        cam.visibility = View.INVISIBLE

        supportFragmentManager
            .beginTransaction()
            .disallowAddToBackStack()
            .replace(R.id.map, MapView.newInstance())
            .replace(R.id.minimap0, MapView.newInstance())
            .replace(R.id.minicam0, CamView.newInstance())
            .replace(R.id.minicam1, CamView.newInstance())
            .commit()
    }
    override fun onFragmentInteraction(f: CamView) {

    }

    override fun onFragmentInteraction(f: MapView) {

        val v0 = map["m0"]?.view
        val v1 = map["m1"]?.view
        val v2 = map["m2"]?.view

        val p0 = v0?.parent
        val p1 = v1?.parent
        val p2 = v1?.parent


        if (p0 is ViewGroup && p1 is ViewGroup && p2 is ViewGroup) {
            p0.removeView(v0)
            p1.removeView(v1)
            p0.addView(v1)
            p1.addView(v0)

        }
    }
}

interface OnCamFragmentInteractionListener {
    fun onFragmentInteraction(f: CamView)
}

interface OnMapFragmentInteractionListener {
    fun onFragmentInteraction(f: MapView)
}