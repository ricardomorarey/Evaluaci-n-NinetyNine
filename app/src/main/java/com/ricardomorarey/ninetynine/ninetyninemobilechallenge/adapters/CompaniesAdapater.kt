package com.ricardomorarey.ninetynine.ninetyninemobilechallenge.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.ricardomorarey.ninetynine.ninetyninemobilechallenge.models.Companies
import kotlinx.android.synthetic.main.list_view_companies.view.*

class CompaniesAdapater (val context: Context, val layout: Int, val list: List<Companies>) : BaseAdapter(){

    private val mImflator: LayoutInflater = LayoutInflater.from(context)

    override fun getItem(position: Int): Any {
        return  list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view: View
        val viewHolder: CompaniesViewHolder

        if (convertView == null){
            view = mImflator.inflate(layout, parent,false)
            viewHolder = CompaniesViewHolder(view)
            view.tag = viewHolder
        }else{
            view = convertView
            viewHolder = view.tag as CompaniesViewHolder
        }

        viewHolder.company_id.text = "${list[position].id}"
        viewHolder.company_name.text = "${list[position].name}"
        viewHolder.company_ric.text = "${list[position].ric}"
        viewHolder.company_sharePrice.text = "${list[position].sharePrice}"

        return view
    }
}

private class CompaniesViewHolder (view: View){
    val company_id: TextView = view.textView_company_id
    val company_name: TextView = view.textView_company_name
    val company_ric: TextView = view.textView_company_ric
    val company_sharePrice: TextView = view.textView_company_sharePrice
}