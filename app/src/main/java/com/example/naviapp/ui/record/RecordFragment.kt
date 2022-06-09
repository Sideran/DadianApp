package com.example.naviapp.ui.record

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Build
import android.os.Bundle
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.naviapp.R
import com.example.naviapp.activity.MeasureActivity
import com.example.naviapp.databinding.FragmentRecordBinding
import com.example.naviapp.paging3.model.ApiStatus
import com.example.naviapp.paging3.model.PointInfo
import com.example.naviapp.ui.dashboard.DashboardFragment
import com.example.naviapp.utils.Datapp
import com.example.naviapp.utils.Util
import com.example.pickerviewlibrary.picker.TeaPickerView
import com.example.pickerviewlibrary.picker.entity.KVparmer
import com.example.pickerviewlibrary.picker.entity.PickerData
import com.example.pickerviewlibrary.picker.listener.OnPicker1ClickListener
import com.example.pickerviewlibrary.picker.listener.OnPicker2ClickListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.JsonArray
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import java.io.File


open class RecordFragment : Fragment(),View.OnClickListener {

    companion object {
        fun newInstance() = RecordFragment()
    }
    private lateinit var binding:FragmentRecordBinding
    private lateinit var viewModel: RecordViewModel
    private lateinit var ipAddress:EditText
    private lateinit var  showname: EditText
    private  lateinit var showcode:EditText
    private  lateinit var showaddress:EditText
    private  lateinit var showowner:EditText
    private  lateinit var showdirection:EditText
    private  lateinit var showheight:EditText
    private  lateinit var showlatlon:EditText
    private lateinit var info:EditText
    private lateinit var servicePersonName:EditText
    private lateinit var showblock:EditText
    private lateinit var editTextPhone:EditText
    private lateinit var  spintype:Spinner
    private lateinit var pointype:Spinner
    private lateinit var manufactory:Spinner
    private lateinit var watcharea:Spinner
    private lateinit var photoView:ImageView
    private lateinit var rephoto: FloatingActionButton
    val roundCameraDirection:String="全景"
    var dictm: MutableList<String> = ArrayList()
    var dictu: MutableList<String> = ArrayList()
    var dictp: MutableList<String> = ArrayList()
    var dictpos: MutableList<String> = ArrayList()
    var map=HashMap<String,String>()
    var booleanm=false
    var booleanu=false
    var booleanp=false
    var booleanpos=false
    lateinit var Deptdata: JsonArray
    lateinit var ancestor:String
    lateinit var datascope:String
    var myDept=KVparmer("100","deptName")
    var mFirstDatas: MutableList<KVparmer> = ArrayList()
    var mSecondDatas: MutableMap<String, MutableList<KVparmer>> = mutableMapOf()
    var mThirdDatas: MutableMap<String, MutableList<KVparmer>> =  mutableMapOf()
    lateinit var listdept:ImageButton
    lateinit var radiogroup:RadioGroup
    lateinit var radioEnter:RadioButton
    lateinit var radioOuter:RadioButton
    lateinit var manufacture: SpinnerAdapter
    lateinit var ptztype: SpinnerAdapter
    lateinit var useType: SpinnerAdapter
    lateinit var watchAdapter:SpinnerAdapter
    private lateinit var  progressDialog: ProgressDialog
    private lateinit var app:Datapp
    lateinit var bm:Bitmap
    var point=PointInfo("","","","","","","","","","","","",
        "","","","","","","","","","","","","",
        "","","","","","","","","","","","")


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding= FragmentRecordBinding.inflate(inflater, container, false)
        app = requireActivity().application as Datapp
        initView()
        val root: View = binding.root
        setupUI(root)
        return root//inflater.inflate(R.layout.fragment_record, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RecordViewModel::class.java)
//        getDictData()
        setlistener()
    }
    fun initView(){
        progress(false)
        rephoto=binding.floatingActionButton2
        rephoto.visibility=View.VISIBLE
        ipAddress=binding.recordScrolling.Guoatrribute1!!.ipAddress
        showname = binding.recordScrolling.Guoatrribute1!!.showname
        showaddress = binding.recordScrolling.position1!!.showaddress
        showcode = binding.recordScrolling.Guoatrribute1!!.showcode
        showdirection = binding.recordScrolling.position1!!.showdirection
        showlatlon = binding.recordScrolling.position1!!.showlatlon
        showowner = binding.recordScrolling.Guoatrribute1!!.showowner
        showblock=binding.recordScrolling.Guoatrribute1!!.block
        showheight = binding.recordScrolling.position1!!.showheight
        servicePersonName=binding.recordScrolling.management1!!.servicePersonName
        editTextPhone=binding.recordScrolling.management1!!.editTextPhone
        info=binding.recordScrolling.management1!!.info
        spintype = binding.recordScrolling.position1!!.spinnertype
        pointype = binding.recordScrolling.Guoatrribute1!!.pointype
        watcharea=binding.recordScrolling.position1!!.watcharea
        manufactory=binding.recordScrolling.Guoatrribute1!!.manufactory
        photoView=binding.fragmentPhoto
        listdept=binding.recordScrolling.Guoatrribute1!!.listpicker
        rephoto=binding.floatingActionButton2
        radioEnter=binding.recordScrolling.position1!!.radioEnter
        radioOuter=binding.recordScrolling.position1!!.radioOuter
        radiogroup=binding.recordScrolling.position1!!.radiogroup
        binding.recordScrolling.imageView3!!.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
        binding.recordScrolling.Guoatrribute1!!.Guoatrribute.setVisibility(View.VISIBLE)
        binding.recordScrolling.imageView2!!.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
        binding.recordScrolling.imageView1!!.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
        binding.recordScrolling.linearlayout1!!.setOnClickListener(this)
        radiogroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = requireActivity().findViewById(checkedId) as RadioButton
            when(radio){
                radioEnter->{
                    map.set("roomType","室内")
                }
                radioOuter->{
                    map.set("roomType","室外")
                }
            }
        })

    }
    fun setlistener() {

        binding.recordScrolling.linearlayout1!!.setOnClickListener(this)
        binding.recordScrolling.linearlayout2!!.setOnClickListener(this)
        binding.recordScrolling.linearlayout3!!.setOnClickListener(this)
        binding.recordScrolling.upload!!.setOnClickListener(this)
        binding.floatingActionButton2!!.setOnClickListener(this)
        binding.fragmentPhoto!!.setOnClickListener(this)
//        fab.setOnClickListener(listener)
        manufactory.onItemSelectedListener= object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(dictm!=null&&!dictm.get(position).equals("设备厂商")){
                    point.manufacturer=dictm.get(position)
                    map.set("manufacturer",dictm.get(position))
                }else{
                    map.set("manufacturer","")
                }

            }
        }
        spintype.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(dictp!=null&&!dictp.get(position).equals("摄像机类型")) {
                    point.ptzType = dictp.get(position)
                    map.set("ptzType",dictp.get(position))
                    if (dictp[position] == "球机" || dictp[position] == "半球") {
                        showdirection.setText(roundCameraDirection)
                    } else {
                        showdirection.setText(app.direction)
                    }
                }else map.set("ptzType","")
            }
        }
        pointype.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(dictu!=null&&!dictu.get(position).equals("点位类型")){
                    point.useType=dictu.get(position)
                    map.set("useType",dictu.get(position))
                }else  map.set("useType","")

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        watcharea.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(dictpos!=null&&!dictpos.get(position).equals("监控范围")){
                    map.set("positionType",watcharea.selectedItem.toString())
                }else map.set("positionType","")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }


    fun getDictData(){
        dictm.clear()
        dictp.clear()
        dictu.clear()
        dictpos.clear()
        mFirstDatas.clear()
        lifecycleScope.launch {
            viewModel.getDeptList(app.token,"").collect { result->
                when(result.status){
                    ApiStatus.SUCCESS->{
                        Deptdata=result.data!!.asJsonArray
                        ancestor="0,"+Deptdata.get(0).asJsonObject.get("deptId").asString
                        if("1".equals(datascope)){
                            for(item in Deptdata){
                                var firstDept=item.asJsonObject.get("ancestors").asString
                                if(ancestor.equals(firstDept)){
                                    var kvparmer=KVparmer(
                                        item.asJsonObject.get("deptId").asString,
                                        item.asJsonObject.get("deptName").asString)
                                    mFirstDatas.add(kvparmer)
                                }
                            }
                        }else{
                            showowner.setText(myDept.value)
                            map.set("deptName",myDept.value)
                            map.set("deptId",myDept.key)
                            mFirstDatas.add(myDept)
                        }
                        intiPickerView()
                    }
                    ApiStatus.ERROR->{
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewModel.getDict(app.token,"设备厂商").collect { result->
                when(result.status){
                    ApiStatus.SUCCESS->{
                        var jsonArray1=result.data!!.asJsonArray
                        dictm.add("设备厂商")
                        for (item in jsonArray1){
                            dictm.add(item.asJsonObject.get("dataName").asString)
                        }
                        manufacture = ArrayAdapter(requireContext(),  R.layout.spinner_item, dictm)
                        manufactory.adapter=manufacture
                        booleanm=true
                    }
                    ApiStatus.ERROR->{

                        Util.showToast(context,"获取失败")
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewModel.getDict(app.token,"摄像机类型").collect { result->
                when(result.status){
                    ApiStatus.SUCCESS->{
                        dictp.add("摄像机类型")
                        var jsonArray2= result.data!!.asJsonArray
                        for (item in jsonArray2){

                            dictp.add(item.asJsonObject.get("dataName").asString) }
                        ptztype = ArrayAdapter(requireContext(),  R.layout.spinner_item, dictp)
                        spintype.adapter=ptztype
                        booleanp=true
                    }
                    ApiStatus.ERROR->{
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewModel.getDict(app.token,"点位类型").collect { result->
                when(result.status){
                    ApiStatus.SUCCESS->{
                        dictu.add("点位类型")
                        var jsonArray3= result.data!!.asJsonArray
                        for (item in jsonArray3){
                            dictu.add(item.asJsonObject.get("dataName").asString) }
                        useType = ArrayAdapter(requireContext(),  R.layout.spinner_item, dictu)
                        pointype.adapter=useType
                        booleanu=true
                    }
                    ApiStatus.ERROR->{
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewModel.getDict(app.token,"监控范围").collect { result->
                when(result.status){
                    ApiStatus.SUCCESS->{
                        dictpos.add("监控范围")
                        var jsonArray4= result.data!!.asJsonArray
                        for (item in jsonArray4){
                            dictpos.add(item.asJsonObject.get("dataName").asString) }
                         watchAdapter= ArrayAdapter(requireContext(),  R.layout.spinner_item, dictpos)
                        watcharea.adapter=watchAdapter
                        booleanpos=true
                    }
                    ApiStatus.ERROR->{
                    }
                }
            }
        }
    }
    fun intiPickerView(){
        //设置数据有多少层级

        //设置数据有多少层级
        val data = PickerData()
        data.firstDatas = mFirstDatas
        data.setInitSelectText("请选择")
        var teaPickerView = TeaPickerView(activity, data)
        teaPickerView.setScreenH(3)
            .setDiscolourHook(true)
            .setRadius(25)
            .setContentLine(true)
            .setRadius(25)
            .build()
        listdept.setOnClickListener(View.OnClickListener { v: View? ->
            //显示选择器
            teaPickerView.show(listdept)
        })
        //选择器点击事件
        //选择器点击事件
        teaPickerView.setOnPicker1ClickListener(OnPicker1ClickListener { pickerData: PickerData ->
            map.set("deptId",pickerData.fistId.toString())
            showowner.setText(pickerData.firstText)
            var nextFlour=false
            var secondData: MutableList<KVparmer> = ArrayList()
            for(item in Deptdata){
                var secondAnc=item.asJsonObject.get("ancestors").asString
                var ancestor1=ancestor+","+pickerData.fistId.toString()
                if(ancestor1.equals(secondAnc)){
                    nextFlour=true
                    secondData.add(KVparmer(item.asJsonObject.get("deptId").asString,item.asJsonObject.get("deptName").asString))
                }
            }
            if(nextFlour){
                //mSecondDatas.put(pickerData.firstText,secondData)
                teaPickerView.setList(2,secondData)
            }else{
                teaPickerView.dismiss() //关闭选择器
            }

        })
        teaPickerView.setOnPicker2ClickListener(OnPicker2ClickListener { pickerData: PickerData ->
            showblock.setText(pickerData.secondText)
            teaPickerView.dismiss() //关闭选择器
        })
    }
    fun progress(boolean: Boolean){
        if(boolean){
            binding.recordScrolling.Guoatrribute1!!.guoProgressbar.visibility=View.VISIBLE
            binding.recordScrolling.position1!!.posProgressBar.visibility=View.VISIBLE
            binding.recordScrolling.management1!!.manageProgressBar.visibility=View.VISIBLE
        }else{
            binding.recordScrolling.Guoatrribute1!!.guoProgressbar.visibility=View.GONE
            binding.recordScrolling.position1!!.posProgressBar.visibility=View.GONE
            binding.recordScrolling.management1!!.manageProgressBar.visibility=View.GONE
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

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onClick(v: View) {
        when (v.id) {
            R.id.upload -> {
//                var str= JSON.toJSONString(point)
//                var map= Util.jsonToMap(str)
                uploadPoint()
            }
            R.id.floatingActionButton2->{
                val intent= Intent(context,
                    MeasureActivity::class.java)
                var bundle:Bundle= Bundle()
                bundle.putString("facilityId","")
                intent.putExtras(bundle)
                startActivity(intent)
            }
            R.id.fragment_photo-> {
                if(app.timeCode.equals("")){
                    val intent= Intent(context,
                        MeasureActivity::class.java)
                    var bundle:Bundle=Bundle()
                    bundle.putString("facilityId","")
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else if(bm!=null){
                    bigImageLoader(bm)
                }
            }

            R.id.linearlayout1 -> if (binding.recordScrolling.Guoatrribute1!!.Guoatrribute.getVisibility() === View.VISIBLE) {
                binding.recordScrolling.Guoatrribute1!!.Guoatrribute.setVisibility(View.GONE)
                binding.recordScrolling.imageView1!!.setImageResource(R.drawable.ic_baseline_keyboard_arrow_right_24)
            } else {
                binding.recordScrolling.Guoatrribute1!!.Guoatrribute.setVisibility(View.VISIBLE)
                binding.recordScrolling.imageView1!!.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
            R.id.linearlayout2 -> if (binding.recordScrolling.position1!!.position.getVisibility() === View.VISIBLE) {
                binding.recordScrolling.position1!!.position.setVisibility(View.GONE)
                binding.recordScrolling.imageView2!!.setImageResource(R.drawable.ic_baseline_keyboard_arrow_right_24)
            } else {
                binding.recordScrolling.position1!!.position.setVisibility(View.VISIBLE)
                binding.recordScrolling.imageView2!!.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
            R.id.linearlayout3 -> if (binding.recordScrolling.management1!!.management.getVisibility() === View.VISIBLE) {
                binding.recordScrolling.management1!!.management.setVisibility(View.GONE)
                binding.recordScrolling.imageView3!!.setImageResource(R.drawable.ic_baseline_keyboard_arrow_right_24)
            } else {
                binding.recordScrolling.management1!!.management.setVisibility(View.VISIBLE)
                binding.recordScrolling.imageView3!!.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.N)
    private fun uploadPoint(){
        point.name = showname.checkBlank("摄像头名称不能为空") ?: return
        point.owner = showowner.checkBlank("单位不能为空") ?: return
//        point.address = showaddress.checkBlank("地址不能为空") ?: return
        if(app.timeCode==null){Util.showToast(context,"请拍照后上传")}
        showblock.text?.let { map.set("block",showblock.text.toString()) }
        showname.text?.let { map.set("name",showname.text.toString()) }
        showowner.text?.let { map.set("owner",showowner.text.toString()) }
        showblock.text?.let { map.set("block",showblock.text.toString()) }
        showaddress.text?.let { map.set("address",showaddress.text.toString()) }

        showcode.text?.let { point.deviceid = showcode.text.toString()
            map.set("deviceId",showcode.text.toString())}
        ipAddress.text?.let { point.ipAddress=ipAddress.text.toString()
            map.set("ipAddress",ipAddress.text.toString())}
//        app.totalHeight?.let { point.height = app.totalHeight.toString()
//            map.set("height",app.totalHeight.toString() )}
        showheight?.let { point.height = app.totalHeight.toString()
            map.set("height",showheight.text.toString().replace("cm","") )}
        if(app.latLng!=null){
            app.latLng.latitude?.let { point.latitude = app.latLng.latitude.toString()
                map.set("latitude",app.latLng.latitude.toString())}
            app.latLng.longitude?.let { point.longitude = app.latLng.longitude.toString()
                map.set("longitude",app.latLng.longitude.toString() )}
        }
        else{
            showlatlon.text.clear()
            showlatlon.checkBlank("经纬度请通过拍摄获取")?:return
        }
        servicePersonName.text?.let { point.onDuty = servicePersonName.text.toString()
            map.set("onDuty",servicePersonName.text.toString())}
        editTextPhone.text?.let { point.dutyPhone = editTextPhone.text.toString()
            map.set("dutyPhone",editTextPhone.text.toString())}
        info.text?.let { map.set("info",info.text.toString()) }
        app.direction?.let { point.directionType = app.direction.toString()
            map.set("directionType",app.direction.toString())}

        app.timeCode?.let {
            progressDialog = Util.MaskUtil.showProgressDialog("保存中", activity)}
        var display : Display = requireActivity().getWindowManager().getDefaultDisplay()

        var file = File(app.fileUrl + "/" + app.timeCode + ".jpg")
        if(file.exists()){
            var request:RequestBody= RequestBody.create(MediaType.parse("image/png"),file)
            var pic:MultipartBody.Part=MultipartBody.Part.createFormData("picfile",file.name,request)
            lifecycleScope.launch {
                viewModel.uploadPoint(app.token, map , pic).collect { result ->
                    when (result.status) {
                        ApiStatus.SUCCESS -> {
                            progressDialog.dismiss()
                            showDialog("保存成功", "", true)
                        }
                        ApiStatus.ERROR -> {
                            progressDialog.dismiss()
                            showDialog("保存失败", "请检查网络后重试", false)
                        }
                    }
                }
            }
        }else{
            Util.showCenter(context,display,"请拍照获取经纬度及高度后上传")
        }
    }
    private fun showDialog(title:String,msg:String,success:Boolean) {
        val aa = AlertDialog.Builder(requireContext())
        aa.setTitle(title)
        aa.setMessage(msg)
        if(success){
            aa.setIcon(R.drawable.ic_baseline_thumb_up_alt_24)
            aa.setPositiveButton("确定") { dialog, which -> // TODO Auto-generated method stub
                clean()
            }
//        aa.setNegativeButton("取消", null)
        }else{
            aa.setIcon(R.drawable.ic_baseline_notification_important_24)
            aa.setPositiveButton("确定") { dialog, which -> // TODO Auto-generated method stub
              null
            }
        }

        aa.create()
        aa.show()
    }

    //方法里直接实例化一个imageView不用xml文件，传入bitmap设置图片
    private fun bigImageLoader(bitmap: Bitmap) {
        val dialog = activity?.let { Dialog(it) }
        val image = ImageView(context)
        var matrix:Matrix= Matrix()
        matrix.postScale(3f,3f); //长和宽放大缩小的比例
        var resizeBmp:Bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        image.setImageBitmap(resizeBmp)
        dialog!!.setContentView(image)
        //将dialog周围的白块设置为透明
        dialog.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
        //显示
        dialog.show()
        //点击图片取消
        image.setOnClickListener { dialog.cancel() }
    }


    override fun onResume(){
        super.onResume()
        if(app.isLogin){
            datascope=app.dataScope
            myDept.key=app.deptId
            myDept.value=app.deptName
            if(!booleanm||!booleanp||!booleanu||!booleanpos){
//                reCreate()
                getDictData()
            }
        }
        if (!app.fileUrl .equals("")  && !app.timeCode.equals("")) {
            var file= File(app.fileUrl + "/" + app.timeCode + ".jpg")
            if(file.exists()){
                bm = Util.decodeBitmapForFile(app.fileUrl + "/" + app.timeCode + ".jpg",640,960)
                if (bm != null) photoView.setImageBitmap(bm)
                rephoto.visibility=View.VISIBLE
            }

        }
        if (app.totalHeight != 0f) showheight.setText(String.format("%.2f", app.totalHeight) + "cm")
//yzs        if (app.temptLocation != "") showdirection.setText(app.direction)
//yzs        if (app.temptLocation != "" && app.latLng != null) showlatlon.setText(app.latLng.longitude.toString() + "," + app.latLng.latitude)

//        if (app.direction != ""&&!roundCameraDirection.equals(showdirection.text)) showdirection.setText(app.direction)
        if (dictp.isNotEmpty()) {
            if (spintype.getSelectedItem().toString() == "球机" || spintype.getSelectedItem()
                    .toString() == "半球" || spintype.getSelectedItem().toString() == "4G布控球"
            ) {
                showdirection.setText(roundCameraDirection)
            } else if (spintype.getSelectedItem().toString() == "摄像机类型") {
                if (!app.direction.equals("")) {
                    showdirection.setText(app.direction)
                }
            } else {
                if (!app.direction.equals("")) {
                    showdirection.setText(app.direction)
                }
            }
        }
        if (app.latLng != null) showlatlon.setText(app.latLng.longitude.toString() + ",\n" + app.latLng.latitude)
        if (app.temptLocation != "") showaddress.setText(app.temptLocation)

    }
    //隐藏软键盘
    fun setupUI(view: View) {
        //Set up touch listener for non-text box views to hide keyboard.
        if (view !is EditText) {
            view.setOnTouchListener { v, event ->
                DashboardFragment.hideSoftKeyboard(context, v) //Main.this是我的activity名
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
    fun clean(){
        app.latLng = null
        app.direction = ""
        app.totalHeight = 0f
        app.temptLocation = ""
        app.timeCode=""
        ipAddress.text.clear()
        showname.text.clear()
        showaddress.text.clear()
        showcode.text.clear()
        showdirection.text.clear()
        showlatlon.text.clear()
        if("1".equals(datascope)){
            showowner.text.clear()
            showblock.text.clear()
        }
        photoView.setImageResource(R.drawable.ic_baseline_add_photo_alternate_24)
        showheight.text.clear()
        servicePersonName.text.clear()
        editTextPhone.text.clear()
        spintype.setSelection(0)
        pointype.setSelection(0)
        watcharea.setSelection(0)
        manufactory.setSelection(0)
//        radiogroup.clearCheck()
    }

    override fun onDestroy() {
        super.onDestroy()
        clean()
    }



}