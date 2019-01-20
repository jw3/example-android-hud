package com.github.jw3.examplehud

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.esri.arcgisruntime.mapping.ArcGISMap
import com.esri.arcgisruntime.mapping.Basemap
import kotlinx.android.synthetic.main.fragment_map_view.*


private const val StrIdParam = "strIdParam"

class MapView : Fragment() {
    private var listener: OnMapFragmentInteractionListener? = null
    private var strId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            strId = it.getString(StrIdParam)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val f = inflater.inflate(R.layout.fragment_map_view, container, false)

        f.findViewById<com.esri.arcgisruntime.mapping.view.MapView>(R.id.mapView)?.let {
            val map = ArcGISMap(Basemap.Type.IMAGERY, 34.056295, -117.195800, 16)
            it.map = map
            it.isAttributionTextVisible = false
        }
        return f
    }

    fun idStr(): String? {
        return strId
    }

    fun onButtonPressed() {
        listener?.onFragmentInteraction(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnMapFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {
        @JvmStatic
        fun newInstance(strId: String) =
            MapView().apply {
                arguments = Bundle().apply {
                    putString(StrIdParam, strId)
                }
            }
    }
}
