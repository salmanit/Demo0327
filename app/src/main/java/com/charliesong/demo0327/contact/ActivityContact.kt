package com.charliesong.demo0327.contact

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import com.charliesong.demo0327.base.BaseActivity
import com.charliesong.demo0327.base.BaseRvAdapter
import com.charliesong.demo0327.base.BaseRvHolder
import com.charliesong.demo0327.R
import com.charliesong.demo0327.base.RvItemTouchListener
import com.charliesong.demo0327.util.RippleAnimation
import kotlinx.android.synthetic.main.activity_contact.*
import java.util.*
import kotlin.Comparator

/**
 * Created by charlie.song on 2018/4/3.
 */
class ActivityContact: BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
        defaultSetTitle("contacts")
        initSearch()
        initData()
        initRecyclerView()
        fab_mode.setOnClickListener {
            RippleAnimation.anchor(it).setDuration(2000).startRipple()
            changeMode()
        }

    }
    var color= Color.RED
    private fun changeMode(){
      color=if(color==Color.RED) Color.BLUE else Color.RED
        (rv_contact.getItemDecorationAt(0) as ItemDecorationC).apply {
            paintFloatBgText.color=color
        }
        rv_contact.adapter.notifyDataSetChanged()
        supportActionBar?.run {
            setTitle("change..$color")
            this.setBackgroundDrawable(ColorDrawable(color))
        }
    }

    private fun initRecyclerView() {
        rv_contact.layoutManager=LinearLayoutManager(this).apply { findFirstCompletelyVisibleItemPosition() }
        rv_contact.addItemDecoration(ItemDecorationC().apply { datas=contactsFilter })
        rv_contact.adapter= object: BaseRvAdapter<Contact>(){
            override fun getLayoutID(viewType: Int): Int {
                return R.layout.item_contact
            }

            override fun onBindViewHolder(holder: BaseRvHolder, position: Int) {
                var contact=getItemData(position)
                holder.setText(R.id.tv_name,contact.name)
                holder.setText(R.id.tv_phone,contact.phone+" ${contact.py}  ${contact.index}")
                holder.setImageUrl(R.id.iv_head,contact.head)
            }

            override fun onBindViewHolder(holder: BaseRvHolder, position: Int, payloads: MutableList<Any>) {
                if(payloads.isEmpty()){
                    super.onBindViewHolder(holder, position, payloads)
                }else{
                    holder.setText(R.id.tv_test,"${payloads[0]}")
                }
            }
        }.apply {
        setData(contactsFilter)
        }
        rv_contact.addOnItemTouchListener(RvItemTouchListener(rv_contact).apply {
            listener=object : RvItemTouchListener.RvItemClickListener {
                override fun singleTab(position: Int, viewHolder: RecyclerView.ViewHolder) {
                    rv_contact.adapter.notifyItemChanged(position,"$position")
                }

                override fun longPress(position: Int) {
                }
            }
        })

//        rv_contact.itemAnimator= SlideInLeftAnimator()

    }
//    private fun getAdapter():BaseRvAdapter<Contact>{
//        return rv_contact.adapter as BaseRvAdapter<Contact>
//    }
    private fun initData() {
        contacts.add(Contact("https://upload.jianshu.io/users/upload_avatars/3994917/ae996b9a-5391-496f-b209-3367fb5b0112.png?imageMogr2/auto-orient/strip|imageView2/1/w/96/h/96","10086","张三"))
        contacts.add(Contact(phone="13838000",name = "大桥"))
        contacts.add(Contact(phone="1388000",name = "阿娇"))
        contacts.add(Contact(phone="1380000",name = "博士伦"))
        contacts.add(Contact(phone="138000",name = "波力海苔"))
        contacts.add(Contact(phone="13800138000",name = "鸿门宴"))
        contacts.add(Contact(phone="138001000",name = "墨迹天气"))
        contacts.add(Contact(phone="1380138000",name = "干啥"))
        contacts.add(Contact(phone="138001000",name = "遭到"))
        contacts.add(Contact(phone="138008000",name = "阿姨"))
        contacts.add(Contact(phone="138138000",name = "气球"))
        contacts.add(Contact(phone="13800138000",name = "网球"))
        contacts.add(Contact(phone="138008000",name = "棒球"))
        contacts.add(Contact(phone="138100100",name = "羽毛球"))
        contacts.add(Contact(phone="138001100",name = "今天"))
        contacts.add(Contact(phone="138002100",name = "求求"))
        contacts.add(Contact(phone="138020100",name = "毛毛"))
        contacts.add(Contact(phone="1380010110",name = "黑字"))

        Collections.sort(contacts,object :Comparator<Contact>{
            override fun compare(o1: Contact, o2: Contact): Int {
                return  o1.py.compareTo(o2.py)
            }
        })
        contactsFilter.addAll(contacts)
    }
    var contacts= ArrayList<Contact>()
    var contactsFilter= ArrayList<Contact>()
    fun initSearch(){
        et_search.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable) {
                var filter=s.toString();
                contactsFilter.clear()
                if(TextUtils.isEmpty(s)){
                    contactsFilter.addAll(contacts)
                }else{
                    var result=  contacts.filter { it.name.contains(s)||it.phone.contains(s) }
                    contactsFilter.addAll(result)
                }
//                getAdapter().initData(contactsFilter)
                println("search===================${contactsFilter.size}")
                rv_contact.adapter.notifyDataSetChanged()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
        })
    }

}