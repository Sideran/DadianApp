package com.example.naviapp.activity.ui.main

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.naviapp.R
import com.example.naviapp.databinding.FragmentMyInfoBinding
import com.example.naviapp.entity.UserInfoEntity
import com.example.naviapp.paging3.model.ApiStatus
import com.example.naviapp.utils.Datapp
import com.example.naviapp.utils.Util
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyInfoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding:FragmentMyInfoBinding
    private lateinit var app:Datapp
    private lateinit var viewModel: PageViewModel
    private var userInfo:UserInfoEntity= UserInfoEntity()
    private lateinit var nickname:EditText
    private lateinit var phoneNum:EditText
    private lateinit var email:EditText
    private lateinit var radioGroup:RadioGroup
    private  lateinit var male:RadioButton
    private  lateinit var female:RadioButton
    private lateinit var saveInfo:Button
    private lateinit var infoProgresssBar:ProgressBar
    private lateinit var display:Display
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }
    fun initView(){
        nickname=binding.showNickname
        phoneNum=binding.showPhone
        email=binding.showEmail
        radioGroup=binding.radioGroup
        male=binding.male
        female=binding.female
        saveInfo=binding.saveInfo
        infoProgresssBar=binding.infoProgressBar
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        display = activity?.windowManager?.getDefaultDisplay()!!
        binding= FragmentMyInfoBinding.inflate(inflater, container, false)
        app = requireActivity().application as Datapp
        initView()
        val root: View = binding.root
        return root//inflater.inflate(R.layout.fragment_my_info, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PageViewModel::class.java)


    }
    fun setlistener(){
        nickname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                userInfo.nickName= nickname.text.toString()
                app.nickName=nickname.text.toString()
            }
        })
        phoneNum.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                userInfo.phonenumber=phoneNum.text.toString()
                app.phoneNum=phoneNum.text.toString()
            }
        })
        email.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                userInfo.email = email.text.toString()
                app.email=email.text.toString()
            }
            })
        radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = requireActivity().findViewById(checkedId) as RadioButton
            when(radio){
                male->{
                    userInfo.sex="0"
                    app.sex="0"
                }
                female->{
                    userInfo.sex="1"
                    app.sex="1"
                }
            }
        })
       saveInfo.setOnClickListener {
           nickname.checkBlank("昵称不能为空") ?: return@setOnClickListener
           phoneNum.checkBlank("手机号码不能为空") ?: return@setOnClickListener
           email.checkBlank("邮箱不能为空") ?: return@setOnClickListener
//           val context1:Context=MyInfoFragment().requireContext()
           infoProgresssBar.visibility=View.VISIBLE
           lifecycleScope.launch {
               view?.let { it1 -> hideSoftKeyboard(requireContext(), it1) }
               viewModel.modifyInfo(app.token,userInfo).collect { result->
                   infoProgresssBar.visibility=View.INVISIBLE
                   when(result.status){
                       ApiStatus.SUCCESS->{
//                           ToastDialog(context1,"保存成功","基本信息已保存", R.drawable.success)
                           view?.let { it1 ->
                               Snackbar.make(it1, "保存成功", Snackbar.LENGTH_LONG)
                                   .setAction("Action", null).show()
                           }
                       }
                       ApiStatus.ERROR->{
                           result.message?.let { it2 ->
//                               ToastDialog(context1,"保存失败", it2, R.drawable.fail)
                               view?.let { it1 ->
                                   Snackbar.make(it1, it2, Snackbar.LENGTH_LONG)
                                       .setAction("Action", null).show()
                               }
                           }
                       }
                   }
               }
           }
       }
    }

    private fun ToastDialog(context: Context, title: String, msg: String,src:Int) {
        val td = AlertDialog.Builder(context)
        td.setTitle(title)
        td.setMessage(msg)
        td.setIcon(src)//R.drawable.ic_baseline_notification_important_24
        td.setPositiveButton("确定", null)
    }
    // 编写一个扩展方法
    fun TextView.checkBlank(message: String): String? {
        val text = this.text.toString()
        if (text.isBlank()) {
            this.requestFocus()
            this.setError(message)

            return null
        }
        return text
    }
    override fun onResume(){
        super.onResume()
        if(app.isLogin){
            userInfo.userId=app.userId
            nickname.setText(app.nickName)
            phoneNum.setText(app.phoneNum)
            email.setText(app.email)
            if(app.sex!=null&&!"2".equals(app.sex)){
                if("0".equals(app.sex))male.setChecked(true)
                if("1".equals(app.sex))female.setChecked(true)
            }
        }
        setlistener()
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MyInfoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyInfoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
        /**
         * 隐藏软键盘(可用于Activity，Fragment)
         */
        fun hideSoftKeyboard(context: Context?, v: View) {
            val inputMethodManager = context!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(v.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}