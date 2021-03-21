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

    fun initView() {
        binding.etUserName.showKeyboard()
        binding.ivSearch.setOnClickListener {
            val username = binding.etUserName.text.toString()
            lifecycleScope.launch { loadUser(username) }
        }
    }

    private suspend fun loadUser(userName : String) {
        binding.etUserName.hideKeyboard()
        binding.pbLoader.show()
        try {
            val user = gitHubService.getUser(userName)
//            withContext(Dispatchers.Main){showUserOnUi(user, userName)}
            showUserOnUi(user, userName)
        }catch (e : Exception){
            toast(e.message ?: "Some Error")
        }finally {
            binding.pbLoader.hide()
        }

    }

    private fun showUserOnUi(user: UserModel, userLogIn : String) {

        binding.cvProfileSection.show()

        with(binding.profileSectionLayout){
            tvUserName.text = user.name ?: "${userLogIn}"
            tvUserBio.text = user.bio ?: "BIO is not added by user"
            tvUserLocation.text = user.location ?: "Location not found"
            tvUserFollowersCount.text = "${user.followersCount}"
            tvUserRepositoryCount.text = "${user.RepositoryCount}"

            ivUserImage.load(user.imageUrl)

            btnVisitGitHubPage.setOnClickListener {
                val link = user.htmlPageUrl
                openGitHubPage(link);
            }

        }

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


