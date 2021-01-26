package com.example.scorecounter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scorecounter.databinding.LeaderboardsScreenLayoutBinding


class LeaderboardsActivity : AppCompatActivity() {
    private lateinit var binding: LeaderboardsScreenLayoutBinding
    private val teamAdapter by lazy {
        winnerList.sortByDescending {
            it.score
        }
        TeamAdapter(winnerList, TeamAdapter.WINNER_ITEM_TYPE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LeaderboardsScreenLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.leaderboards_title)

        setRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.leaderboards_screen_menu, menu)
        if (winnerList.isEmpty()) {
            menu?.findItem(R.id.clear_list)?.isEnabled = false

        }
        return true
    }


    private fun setRecyclerView() {
        binding.rvWinnersList.adapter = teamAdapter
        binding.rvWinnersList.layoutManager = LinearLayoutManager(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.clear_list -> {
                item.isEnabled = false
                teamAdapter.teams.clear()
                teamAdapter.notifyDataSetChanged()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        val winnerList = arrayListOf<Team>()

        fun newIntent(context: Context): Intent = Intent(
            context,
            LeaderboardsActivity::class.java
        )
    }

}