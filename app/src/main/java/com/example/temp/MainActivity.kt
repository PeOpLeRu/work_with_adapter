package com.example.temp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray

class customAdapter(private var list: List<MainActivity.Person>) : RecyclerView.Adapter<customAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var position: TextView = view.findViewById(R.id.position)
        var name: TextView = view.findViewById(R.id.name)
        var surname: TextView = view.findViewById(R.id.surname)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position % 3 == 0) {
            holder.position.text = position.toString()
        } else
        {
            holder.position.text = ""
        }
        holder.name.text = list[position].name
        holder.surname.text = list[position].surname
    }

    override fun getItemCount(): Int = list.size

    fun update(newData : List<MainActivity.Person>)
    {
        this.list = newData
        this.notifyDataSetChanged()
    }
}

class MainActivity : AppCompatActivity() {

    data class Person(val name : String, val surname : String)

    lateinit var nowCheckedButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val content = assets.open("test.json").bufferedReader().use {it.readText()}

        val jsonArr = JSONArray(content)

        val people : MutableList<Person> = ArrayList(jsonArr.length())

        for (i in 0 until jsonArr.length())
        {
            val obj = jsonArr.getJSONObject(i)

            people.add(Person(
                obj.getJSONObject("name").getString("firstname"),
                obj.getJSONObject("name").getString("lastname")))
        }

        val r_view = findViewById<RecyclerView>(R.id.r_view)
        val adapter = customAdapter(people)

        r_view.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        r_view.adapter = adapter

        // Далее код для сортировок

        val btn_by_pos : Button = findViewById(R.id.btn_by_pos)
        val btn_by_name : Button = findViewById(R.id.btn_by_name)
        val btn_by_surname : Button = findViewById(R.id.btn_by_surname)

        nowCheckedButton = btn_by_pos

        btn_by_pos.setOnClickListener {
            updateButtonColor(btn_by_pos)
            adapter.update(people)
        }

        btn_by_name.setOnClickListener {
            updateButtonColor(btn_by_name)
            val newData = people.sortedBy { it.name }
            adapter.update(newData)
        }

        btn_by_surname.setOnClickListener {
            updateButtonColor(btn_by_surname)
            val newData = people.sortedBy { it.surname }
            adapter.update(newData)
        }


    }

    fun updateButtonColor(eventButton : Button)
    {
        nowCheckedButton.setBackgroundColor(resources.getColor(R.color.teal_700))
        eventButton.setBackgroundColor(resources.getColor(R.color.checked))
        this.nowCheckedButton = eventButton

    }
}