package com.project.claims.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputType
import android.util.ArrayMap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.project.claims.R
import com.project.claims.databinding.*
import com.project.claims.model.IdName
import com.project.claims.viewmodel.SubmitClaimsViewModel
import org.json.JSONArray
import org.json.JSONObject


class SubmitClaimsFragment : Fragment() {

    private lateinit var viewModel:SubmitClaimsViewModel
    private lateinit var binding: SubmitClaimsFragmentBinding
    private val dataMap = ArrayMap<String,JSONArray>()
    private var nonDependentSpinner:Spinner? = null

    /*inflating the layout*/
    override fun onCreateView(inflater:LayoutInflater, container:ViewGroup?, savedInstanceState:Bundle?):View{
        binding = DataBindingUtil.inflate(inflater, R.layout.submit_claims_fragment, container, false);
        binding.handler = this
        return binding.root
    }

    /*init*/
    override fun onActivityCreated(savedInstanceState:Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SubmitClaimsViewModel::class.java)
        getClaimsData()
    }

    /*Method to get json data from viewmodel*/
    private fun getClaimsData() {
        val claimsObject = JSONObject(viewModel.loadJSONFromAsserts("claims_json.json", requireContext()))
        populateClaimType(claimsObject.getJSONArray("Claims"));
    }

    /*Method to populate json data*/
    private fun populateClaimType(claimsArray:JSONArray) {
        val claimTypes = ArrayList<String>();
        for (i in 0..claimsArray.length()-1){
            val claimType = claimsArray.getJSONObject(i).getJSONObject("Claimtype").getString("name")
            claimTypes.add(claimType)
            dataMap.put(claimType,claimsArray.getJSONObject(i).getJSONArray("Claimtypedetail"))
        }
        binding.spinnerClaims.adapter = ArrayAdapter<String>(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, claimTypes)
        onClaimSelected();
    }

    /*Method to populate layout based on claim selected*/
    private fun onClaimSelected() {
        binding.spinnerClaims.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(parent:AdapterView<*>, view:View, pos:Int, id:Long) {
              binding.layoutDynamic.removeAllViews()
              binding.expense.setText("")
              val claimtypedetail : JSONArray = (dataMap.get(binding.spinnerClaims.selectedItem.toString())!!)
              for(i in 0..claimtypedetail.length()-1){
                  val claimField = claimtypedetail.getJSONObject(i).getJSONObject("Claimfield")
                  val label = LayoutTextviewBinding.inflate(LayoutInflater.from(context), null, false)
                  if(claimField.getString( "required").equals("1")) {
                      label.textviewLabel.text = claimField.getString("label") + "*"
                  }else{
                      label.textviewLabel.text = claimField.getString("label")
                  }
                  binding.layoutDynamic.addView(label.root)
                  populateFieldsBasedOnClaimType(claimField, label)
              }
            }
            override fun onNothingSelected(parent:AdapterView<*>?) {}
        })
    }

    /*Method to populate all the fields based on the claim type selcted*/
    private fun populateFieldsBasedOnClaimType(claimField:JSONObject, label: LayoutTextviewBinding) {
        when (claimField.getString("type")) {
            "DropDown" -> {
                val spinner = LayoutSearchableSpinnerBinding.inflate(LayoutInflater.from(context), null, false)
                val list = ArrayList<IdName>()
                if(claimField.getString("isdependant").equals("0")) {
                    for (j in 0..claimField.getJSONArray("Claimfieldoption").length() - 1) {
                        list.add(IdName(claimField.getJSONArray("Claimfieldoption").getJSONObject(j).getString("id"), claimField.getJSONArray("Claimfieldoption").getJSONObject(j).getString("name")))
                    }
                    nonDependentSpinner = spinner.spinnerData
                }
                else {
                    for (j in 0..claimField.getJSONArray("Claimfieldoption").length() - 1) {
                        if((nonDependentSpinner?.selectedItem as IdName).id.equals(claimField.getJSONArray("Claimfieldoption").getJSONObject(j).getString("belongsto"))) {
                            list.add(IdName(claimField.getJSONArray("Claimfieldoption").getJSONObject(j).getString("id"), claimField.getJSONArray("Claimfieldoption").getJSONObject(j).getString("name")))
                        }
                    }
                    onDynamicSpinnerSelected(nonDependentSpinner, claimField, spinner.spinnerData, label.textviewLabel)
                }
                spinner.spinnerData.adapter = ArrayAdapter<IdName>(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, list)
                binding.layoutDynamic.addView(spinner.root)
            }
            "SingleLineTextAllCaps" -> {
                val capsField = LayoutEdittextBinding.inflate(LayoutInflater.from(context), null, false)
                capsField.edittextData.inputType = InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
                capsField.edittextData.tag = claimField.getString( "required")
                binding.layoutDynamic.addView(capsField.root)
            }
            "SingleLineTextNumeric" -> {
                val numericField = LayoutEdittextBinding.inflate(LayoutInflater.from(context), null, false)
                numericField.edittextData.inputType = InputType.TYPE_CLASS_NUMBER
                numericField.edittextData.tag = claimField.getString( "required")
                binding.layoutDynamic.addView(numericField.root)
            }
            "SingleLineText" -> {
                val normalField = LayoutEdittextBinding.inflate(LayoutInflater.from(context), null, false)
                normalField.edittextData.tag = claimField.getString( "required")
                binding.layoutDynamic.addView(normalField.root)
            }
        }
    }

    /*on click of dynamically added spinner - if any data is dependent then the spinner data contains
    * only those data which belongs to that particular spinner*/
    private fun onDynamicSpinnerSelected(spinnerData:Spinner?, claimField:JSONObject, dependentSpinner:Spinner, textviewLabel:TextView) {
        spinnerData?.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(parent:AdapterView<*>, view:View, pos:Int, id:Long) {
                val list = ArrayList<IdName>()
                    for (j in 0..claimField.getJSONArray("Claimfieldoption").length() - 1) {
                        if((nonDependentSpinner?.selectedItem as IdName).id.equals(claimField.getJSONArray("Claimfieldoption").getJSONObject(j).getString("belongsto"))) {
                            list.add(IdName(claimField.getJSONArray("Claimfieldoption").getJSONObject(j).getString("id"), claimField.getJSONArray("Claimfieldoption").getJSONObject(j).getString("name")))
                        }
                    }
                if(list.size > 0) {
                    textviewLabel.visibility = View.VISIBLE
                    dependentSpinner.visibility = View.VISIBLE
                    dependentSpinner.adapter = ArrayAdapter<IdName>(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, list)
                }else{
                    textviewLabel.visibility = View.GONE
                    dependentSpinner.visibility = View.GONE
                }
            }
            override fun onNothingSelected(parent:AdapterView<*>?) {}
        })
    }

    /*to pick date*/
    fun pickDate(view:View){
       viewModel.pickDateFromTo(requireContext(), view as TextView)
    }

    /*on click of add cliam button*/
    fun addClaimItem(view: View){
        if(isMandatoryFieldFilled()) {
            if(isDynamicLayoutMandatoryFieldFilled()) {
                val layoutClaimedItemBinding = LayoutClaimedItemBinding.inflate(LayoutInflater.from(context), null, false)
                layoutClaimedItemBinding.claimTypeData.setText(binding.spinnerClaims.selectedItem.toString())
                layoutClaimedItemBinding.claimDateData.setText(binding.date.text.toString())
                layoutClaimedItemBinding.expenseAmtData.setText(binding.expense.text!!.trim().toString())
                binding.layoutClaimedItem.addView(layoutClaimedItemBinding.root)
            }else{
                Toast.makeText(requireContext(),"Please enter mandatory fields data",Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(requireContext(),"Please enter date and expense",Toast.LENGTH_SHORT).show()
        }
    }

    /*to validate dynamic fields data filled or not*/
    private fun isDynamicLayoutMandatoryFieldFilled():Boolean {
        for(i in 0..binding.layoutDynamic.childCount-1){
            if(binding.layoutDynamic.getChildAt(i).tag != null){
                val ediitextData = binding.layoutDynamic.getChildAt(i) as EditText
                if(ediitextData.tag.equals("1") && ediitextData.text.toString().trim().isEmpty()){
                    return false
                }
            }
        }
        return true
    }

    /*to check mandatory fields filled or not*/
    private fun isMandatoryFieldFilled():Boolean {
        return binding.date.text.toString().isNotEmpty() &&
                binding.expense.text.toString().isNotEmpty()
    }
}

