package com.example.naviapp.activity.ui.main

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.naviapp.R
import com.example.naviapp.databinding.FragmentMyInfoBinding
import com.example.naviapp.databinding.FragmentMyPwdBinding
import com.example.naviapp.paging3.model.ApiStatus
import com.example.naviapp.ui.dashboard.DashboardFragment
import com.example.naviapp.utils.Datapp
import com.example.naviapp.utils.PasswordCheckUtil
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyPwdFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyPwdFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentMyPwdBinding
    private lateinit var app: Datapp
    private lateinit var viewModel: PageViewModel
    private lateinit var pwdProgresssBar: ProgressBar
    private lateinit var oldPassword:EditText
    private lateinit var newPassword:EditText
    private lateinit var confirmPWD:EditText
    private lateinit var savePwd:Button
    private lateinit var visable:ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentMyPwdBinding.inflate(inflater, container, false)
        app = requireActivity().application as Datapp
        val root: View = binding.root
        initView()
        // Inflate the layout for this fragment
        return root// inflater.inflate(R.layout.fragment_my_pwd, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PageViewModel::class.java)
    }
    fun initView(){
        oldPassword=binding.oldPwd
        newPassword=binding.newPwd
        confirmPWD=binding.newConfirm
        pwdProgresssBar=binding.pwdProgressBar
        pwdProgresssBar.visibility=View.INVISIBLE
        savePwd=binding.savePwd
        visable=binding.visable
    }
    fun setlistener(){
        visable.setOnClickListener{
            val textPassword = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            val textVisiblePassword = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            if(oldPassword.inputType==textVisiblePassword){
                visable.setImageResource(R.drawable.eye_24)
                oldPassword.inputType= textPassword
                newPassword.inputType= textPassword
                confirmPWD.inputType= textPassword
            }else{
                visable.setImageResource(R.drawable.blue_eye)
                oldPassword.inputType=textVisiblePassword
                newPassword.inputType=textVisiblePassword
                confirmPWD.inputType=textVisiblePassword
            }
        }
        newPassword.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                newPassword.checkError()?:return
            }

        })
        savePwd.setOnClickListener {
            oldPassword.checkBlank("请输入旧密码")?: return@setOnClickListener
            confirmPWD.checkBlank("请确认新密码")?: return@setOnClickListener
            newPassword.checkError()?:return@setOnClickListener
            if(newPassword.text.toString().equals(oldPassword.text.toString())){
                newPassword.requestFocus()
                newPassword.setError("新密码不能与旧密码相同")
                return@setOnClickListener
            }
            if(!confirmPWD.text.toString().equals(newPassword.text.toString())){
                confirmPWD.requestFocus()
                confirmPWD.setError("两次密码输入不一致")
                return@setOnClickListener
            }
            lifecycleScope.launch {
                view?.let { it1 -> hideSoftKeyboard(it1) }
                pwdProgresssBar.visibility=View.VISIBLE
                viewModel.modifPwd(app.token,oldPassword.text.toString(),newPassword.text.toString()).collect {result->
                    pwdProgresssBar.visibility=View.INVISIBLE
                    when(result.status){
                        ApiStatus.SUCCESS->{
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
    fun TextView.checkError(): String? {
        val text = this.text.toString()
        if (text.isBlank()) {
            this.requestFocus()
            this.setError("密码不能为空")
            return null
        }
        else if(!PasswordCheckUtil.checkPasswordLength(text)){
            this.requestFocus()
            this.setError("密码长度应为5~20位")
            return null
        }
        else if(!PasswordCheckUtil.checkContainDigit(text)||!PasswordCheckUtil.checkContainLowerCase(text)
            ||!PasswordCheckUtil.checkContainUpperCase(text)){
            this.requestFocus()
            this.setError("密码应包含大小写字母及数字")
            return null
        }
        return text
    }
    override fun onResume(){
        super.onResume()
        if(app.isLogin){

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
         * @return A new instance of fragment MyPwdFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyPwdFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    /**
     * 隐藏软键盘(可用于Activity，Fragment)
     */
    fun hideSoftKeyboard( v: View) {
        val inputMethodManager = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(v.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}