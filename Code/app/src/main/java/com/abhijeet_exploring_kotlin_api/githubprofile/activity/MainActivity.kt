package com.abhijeet_exploring_kotlin_api.githubprofile.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import coil.load
import com.abhijeet_exploring_kotlin_api.githubprofile.R
import com.abhijeet_exploring_kotlin_api.githubprofile.databinding.ActivityMainBinding
import com.abhijeet_exploring_kotlin_api.githubprofile.model.UserModel
import com.abhijeet_exploring_kotlin_api.githubprofile.service.createGitHubService
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val gitHubService by lazy { createGitHubService() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

    }

    private fun initView() {
        binding.ivSearch.setOnClickListener {
            binding.pbLoader.show()
            val username = binding.etUserName.text.toString()
            if(username.isNotEmpty()){
                binding.etUserName.hideKeyboard()
                lifecycleScope.launch { loadUser(username) }
            }else{
                binding.pbLoader.hide()
                toast("Give valid User Name")
            }
        }

        binding.ivClear.setOnClickListener { binding.etUserName.text.clear() }
    }

    private suspend fun loadUser(userName : String) {
        try {
            val user = gitHubService.getUser(userName)
            showUserOnUi(user, userName)
        }catch (e : Exception){
            binding.pbLoader.hide()
            val msg = if(e.message.toString().contains("404")) { "No such User found" }
                      else "Check your Internet Connection"
            toast(msg)
        }

    }

    private fun showUserOnUi(user: UserModel, userLogIn : String) {
        binding.pbLoader.hide()
        binding.cvProfileSection.show()

        with(binding.profileSectionLayout){
            tvUserName.text = user.name ?: "${userLogIn}"
            tvUserBio.text = user.bio ?: "Nothing to Show !!"
            tvUserLocation.text = user.location ?: "Location not added"
            tvUserFollowersCount.text = "${user.followersCount} Followers"
            tvUserRepositoryCount.text = "${user.RepositoryCount} Public Repos."

            ivUserImage.load(user.imageUrl)

            btnVisitGitHubPage.setOnClickListener {
                val link = user.htmlPageUrl
                openGitHubPage(link);
            }

            ivShare.setOnClickListener {
                var uName =  user.name ?: userLogIn
                uName = "User : $uName"
                val htmlLink = user.htmlPageUrl
                shareUser(uName, htmlLink)
            }

        }

    }

    private fun shareUser(uName: String, htmlLink: String) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TITLE, "GitHub Profile \n $uName")
            putExtra(Intent.EXTRA_TEXT,"Visit : $htmlLink")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(intent, "Share")
        startActivity(shareIntent)
    }

    private fun openGitHubPage(link: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(link)
        startActivity(intent)
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
    private fun View.show() {
        visibility = View.VISIBLE
    }
    private fun View.hide() {
        visibility = View.GONE
    }
    private fun View.showKeyboard() {
        binding.etUserName.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.etUserName, InputMethodManager.SHOW_FORCED)
    }
    private fun View.hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.windowToken, 0)
    }

}


