package com.example.scorecounter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scorecounter.databinding.TeamsScreenLayoutBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: TeamsScreenLayoutBinding
    private val teamAdapter by lazy {
        TeamAdapter(ArrayList())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TeamsScreenLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setRecyclerView()
        setButtonClickListener()
    }

    private fun setButtonClickListener() {
        binding.btnStartGame.setOnClickListener {
            startActivity(ScoreActivity.newIntent(this, teamAdapter.teams))
        }
    }

    private fun setRecyclerView() {
        binding.rvTeams.adapter = teamAdapter
        binding.rvTeams.layoutManager = LinearLayoutManager(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.teams_screen_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_team -> {
                if (teamAdapter.teams.size == 2) {
                    Toast.makeText(
                        this,
                        getString(R.string.teams_already_created),
                        Toast.LENGTH_SHORT
                    ).show()
                    return true
                }
                showDialog()
                true
            }
            R.id.leaderboards -> {
                val intent = LeaderboardsActivity.newIntent(this)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDialog() {
        val dialog = CreateTeamDialog()
        dialog.setCallback { name, color ->
            teamAdapter.teams.add(Team(name, color))
            teamAdapter.notifyItemInserted(teamAdapter.itemCount)

            if (teamAdapter.teams.size == 2) {
                binding.btnStartGame.visibility = View.VISIBLE
            }
        }

        supportFragmentManager.beginTransaction()
            .add(dialog, "CREATE_TEAM")
            .commitAllowingStateLoss()
    }

    companion object {
        fun newIntent(context: Context): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            return intent
        }
    }
}