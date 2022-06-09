package com.example.naviapp.ui.dashboard

import com.example.naviapp.utils.Datapp
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import android.content.Intent
import com.example.naviapp.activity.ScrollingActivity
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.View.GONE
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.naviapp.Adapter.PointPageAdapter
import com.example.naviapp.R
import com.example.naviapp.databinding.FragmentDashboardBinding
import com.example.naviapp.entity.KVparmer
import com.example.naviapp.paging3.FooterAdapter
import com.example.naviapp.paging3.Repository
import com.example.naviapp.paging3.model.ApiStatus
import com.example.naviapp.utils.Util
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class DashboardFragment : Fragment() {
    private val dashboardViewModel by lazy { ViewModelProvider(this).get(DashboardViewModel::class.java) }

    val data: String? = null
    lateinit var dataScope:String
    var dictm=ArrayList<String>()
    var dictu=ArrayList<String>()
    var dictp=ArrayList<String>()
    var dictpos=ArrayList<String>()
    var deptList= ArrayList<KVparmer>()
    lateinit var spinner1:Spinner
    lateinit var spinner2:Spinner
    lateinit var spinner3:Spinner
    lateinit var spinner4:Spinner
    lateinit var manufacture:SpinnerAdapter
    lateinit var ptztype:SpinnerAdapter
    lateinit var useType:SpinnerAdapter
    lateinit var postionType:SpinnerAdapter
    private lateinit var binding : FragmentDashboardBinding
    lateinit var pointList:RecyclerView
    lateinit var searchView:SearchView
    private val pageAdapter = PointPageAdapter()
    lateinit var progressBar:ProgressBar
    var booleanm=false
    var booleanu=false
    var booleanp=false
    var booleanpos=false
    val bundle: Bundle= Bundle()
    lateinit var app:Datapp
    lateinit var searchParmer:HashMap<String,String>
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {


        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        app=Datapp.getContext() as Datapp;

        /**
         * Call this function whenever you want to check user login
         * This will redirect user to LoginActivity is he is not
         * logged in
         */
        initView()

        searchView.onActionViewExpanded()
        searchView.isSubmitButtonEnabled

//        context = activity
//        pointEntityList = Util.simulate(context, "point.json")

        pointList.itemAnimator = DefaultItemAnimator() // 设置Item之间间隔样式(分割线)
        pointList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        pointList.adapter = pageAdapter.withLoadStateFooter(FooterAdapter { pageAdapter.retry() })
        //设置点击事件
        pageAdapter.onItemClick={v, pos, pointInfo ->
            val intent=Intent(context,
                ScrollingActivity::class.java)
            bundle.putString("facilityId",pointInfo.facilityId)
            intent.putExtras(bundle)
            startActivity(intent)
        }

        searchOption()
        //隐藏软键盘
        setupUI(root)


        return root
    }
    fun getDictData(){
        dictm.clear()
        dictp.clear()
        dictu.clear()
        dictpos.clear()
        deptList.clear()
        if("1".equals(dataScope)){
            lifecycleScope.launch{
                dashboardViewModel.getDeptList(app.token,"").collect { result->
                    when(result.status){
                        ApiStatus.SUCCESS->{
                            var jsonArray=result.data!!.asJsonArray
                            var ancestor="0,"+jsonArray.get(0).asJsonObject.get("deptId").asString
                            var kv=KVparmer(jsonArray.get(0).asJsonObject.get("deptId").asString,jsonArray.get(0).asJsonObject.get("deptName").asString)
                            deptList.add(kv)
                            for (item in jsonArray){
                                var firstDept=item.asJsonObject.get("ancestors").asString
                                if(ancestor.equals(firstDept)){
                                    var kvparmer=KVparmer(item.asJsonObject.get("deptId").asString,item.asJsonObject.get("deptName").asString)
                                    deptList.add(kvparmer)
                                }
                            }
                            var dept=ArrayAdapter(requireContext(),R.layout.spinner_item,deptList)
                            spinner1.adapter=dept
                            booleanm=true
                        }
                        ApiStatus.ERROR->{

                            Util.showToast(context,"尚未登录")
                        }
                    }
                }
            }
        }else{
            lifecycleScope.launch {
                dashboardViewModel.getDict(app.token,"设备厂商").collect { result->
                    when(result.status){
                        ApiStatus.SUCCESS->{
                            var jsonArray1=result.data!!.asJsonArray
                            dictm.add("设备厂商")
                            for (item in jsonArray1){
                                dictm.add(item.asJsonObject.get("dataName").asString)
                            }
                            manufacture = ArrayAdapter(requireContext(),  R.layout.spinner_item, dictm)
                            spinner1.adapter=manufacture
                            booleanm=true
                        }
                        ApiStatus.ERROR->{

                            Util.showToast(context,"尚未登录")
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            dashboardViewModel.getDict(app.token,"摄像机类型").collect { result->
                when(result.status){
                    ApiStatus.SUCCESS->{
                        dictp.add("摄像机类型")
                        var jsonArray2= result.data!!.asJsonArray
                        for (item in jsonArray2){

                            dictp.add(item.asJsonObject.get("dataName").asString) }
                        ptztype = ArrayAdapter(requireContext(),  R.layout.spinner_item, dictp)
                        spinner2.adapter=ptztype
                        booleanp=true
                    }
                    ApiStatus.ERROR->{
                    }
                }
            }
        }
        lifecycleScope.launch {
            dashboardViewModel.getDict(app.token,"点位类型").collect { result->
                when(result.status){
                    ApiStatus.SUCCESS->{
                        dictu.add("点位类型")
                        var jsonArray3= result.data!!.asJsonArray
                        for (item in jsonArray3){
                            dictu.add(item.asJsonObject.get("dataName").asString) }
                        useType = ArrayAdapter(requireContext(),  R.layout.spinner_item, dictu)
                        spinner3.adapter=useType
                        booleanu=true
                    }
                    ApiStatus.ERROR->{
                    }
                }
            }
        }
        lifecycleScope.launch {
            dashboardViewModel.getDict(app.token,"监控范围").collect { result->
                when(result.status){
                    ApiStatus.SUCCESS->{
                        dictpos.add("监控范围")
                        var jsonArray4= result.data!!.asJsonArray
                        for (item in jsonArray4){
                            dictpos.add(item.asJsonObject.get("dataName").asString) }
                        postionType= ArrayAdapter(requireContext(),  R.layout.spinner_item, dictpos)
                        spinner4.adapter=postionType
                        booleanpos=true
                    }
                    ApiStatus.ERROR->{
                    }
                }
            }
        }
        setListener()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        getDictData()

    }
    private fun initView() {
        searchView = binding.searchView
        searchView.onActionViewExpanded()
        searchView.isSubmitButtonEnabled=true
        searchParmer= HashMap()
        progressBar=binding.listProgressBar
        progressBar.visibility=GONE
        pointList = binding.pointList
        spinner1 = binding.dashboard.spinner1
        spinner3 = binding.dashboard.spinner3
        spinner2 = binding.dashboard.spinner2
        spinner4 = binding.dashboard.spinner4


    }
    fun setListener(){
        spinner1.onItemSelectedListener= object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if("1".equals(dataScope)){
                    var kv:KVparmer= spinner1.selectedItem as KVparmer
                    if(!"100".equals(kv.key)) {
                        searchParmer.set("deptId", kv.key)
                        //searchParmer.set("owner", kv.value)
                    }else{
                        searchParmer.set("deptId", "")
                        searchParmer.set("owner", "")
                    }
                }else{
                    if(!dictm.get(position).equals("设备厂商"))
                        searchParmer.set("manufacturer",dictm.get(position))
                    else searchParmer.set("manufacturer","")
                }

            }

        }
        spinner2.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(!dictp.get(position).equals("摄像机类型"))
                    searchParmer.set("ptzType",dictp.get(position))
                else searchParmer.set("ptzType","")
            }

        }
        spinner3.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(!dictu.get(position).equals("点位类型"))
                searchParmer.set("useType",dictu.get(position))
                else{
                    searchParmer.set("useType","")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
        spinner4.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(dictpos!=null&&!dictpos.get(position).equals("监控范围")){
                    searchParmer.set("positionType",spinner4.selectedItem.toString())
                    searchParmer
                }else{
                    searchParmer.set("positionType","")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

    }

    @OptIn(InternalCoroutinesApi::class)
    private fun searchOption( ) {
        val app=Datapp.getContext() as Datapp;
        searchView.setOnSearchClickListener {}
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if(query!=null&&!"".equals(query)){
                    searchParmer.set("keyword",query)
                }else{
                    searchParmer.set("keyword","")
                }
                  // 提交文本时调用
                searchOperate()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // 文本搜索框发生变化时调用
                if(newText.equals("")){
                    this.onQueryTextSubmit("");
                }

                return true
            }
        })
        return
    }

    fun searchOperate(){
        lifecycleScope.launch {
            dashboardViewModel.getPagingData(searchParmer,app.token).collect { pagingData ->
                pageAdapter.submitData(pagingData)
            }

        }
        pageAdapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.NotLoading -> {
                    progressBar.visibility = View.GONE
                    pointList.visibility = View.VISIBLE
                }
                is LoadState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                    pointList.visibility = View.INVISIBLE
                }
                is LoadState.Error -> {
                    val state = it.refresh as LoadState.Error
                    progressBar.visibility = View.INVISIBLE
                    Util.showToast(context, "Load Error: ${state.error.message}")
                }
            }
        }
    }

    //隐藏软键盘
    fun setupUI(view: View) {
        //Set up touch listener for non-text box views to hide keyboard.
        if (view !is EditText) {
            view.setOnTouchListener { v, event ->
                hideSoftKeyboard(context, v) //Main.this是我的activity名
                false
            }
        }
        //If a layout container, iterate over children and seed recursion.
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                setupUI(innerView)
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
//        binding = null
    }

    override fun onResume() {
        super.onResume()
        if(app.isLogin){
            dataScope=app.dataScope
            if(!booleanm||!booleanp||!booleanu||!booleanpos){
                Repository.reCreate()
                getDictData()
            }

        }
    }

    companion object {
        /**
         * 隐藏软键盘(可用于Activity，Fragment)
         */
        fun hideSoftKeyboard(context: Context?, v: View) {
            val inputMethodManager = context!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(v.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }


}