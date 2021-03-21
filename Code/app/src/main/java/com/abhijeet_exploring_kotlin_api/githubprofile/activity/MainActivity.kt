package com.abhijeet_exploring_kotlin_api.githubprofile.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import coil.load
import com.abhijeet_exploring_kotlin_api.githubprofile.databinding.ActivityMainBinding
import com.abhijeet_exploring_kotlin_api.githubprofile.model.UserModel
import com.abhijeet_exploring_kotlin_api.githubprofile.service.createGitHubService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    val gitHubService by lazy { createGitHubService() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivSearch.setOnClickListener {
            GlobalScope.launch{
                val userName = binding.etUserName.text.toString()
                if(userName.isNotEmpty())
                    loadUser(userName)
                else
                    toast("Give me the UserName")
            }
        }

    }

    suspend fun loadUser(username : String){
        binding.pbLoader.show()
        binding.cvProfileSection.show()
        try {
            val user = gitHubService.getUser(username)
            showUserOnUi(user)
        }catch (e : Exception){
            toast(e.message ?: "Some error")  //if e.message is null then pass "Some Error" as error message.
        }finally {
            binding.pbLoader.hide()
            binding.cvProfileSection.hide()
        }

    }

    private fun showUserOnUi(user: UserModel) {
        with(binding.profileSectionLayout){
            tvUserName.text = user.name
            tvUserBio.text = user.bio
            tvUserLocation.text = user.location
            tvUserFollowersCount.text = "${user.followersCount}"
            tvUserRepositoryCount.text = "${user.RepositoryCount}"

            ivUserImage.load(user.imageUrl)
        }
    }

    private fun toast(msg : String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show()
    }

    private fun View.show() {
        visibility = View.VISIBLE
    }
    fun View.hide() {
        visibility = View.GONE
    }
}