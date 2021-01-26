package com.example.scorecounter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.scorecounter.databinding.WinnerScreenLayoutBinding

class WinnerActivity : AppCompatActivity() {
    private lateinit var binding: WinnerScreenLayoutBinding
    private var winner: Team? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = WinnerScreenLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = getString(R.string.winner_screen_title)

        setWinnerName()
        setClickListeners()
    }

    private fun setWinnerName() {
        winner = intent.getParcelableExtra(WINNER_NAME_KEY)
        winner?.apply {
            LeaderboardsActivity.winnerList.add(this)
            val winner = SpannableString(getString(R.string.result_victory, this.name))
            winner.setSpan(ForegroundColorSpan(this.colorResource), 0, this.name.length, 0)
            binding.tvWinner.text = winner
        } ?: run {
            binding.tvWinner.text = getString(R.string.draw_placeholder)
        }
    }

    override fun onBackPressed() {
        val intent = MainActivity.newIntent(this)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.winner_screen_menu, menu)
        return true
    }

    private fun shareResults() {
        winner?.let {
            val intent = createShareIntent(getString(R.string.share_victory, it.name, it.score))
            startActivity(intent)
        } ?: run {
            val intent = createShareIntent(getString(R.string.result_draw))
            startActivity(intent)
        }
    }

    private fun createShareIntent(text: String): Intent {
        val winnerIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }

        return Intent.createChooser(winnerIntent, null)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.share_results -> {
                shareResults()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setClickListeners() {
        binding.btnCreateNewGame.setOnClickListener {
            val intent = MainActivity.newIntent(this)
            startActivity(intent)
        }

        binding.btnOpenLeaderBoards.setOnClickListener {
            val intent = LeaderboardsActivity.newIntent(this)
            startActivity(intent)
        }
    }

    companion object {
        const val WINNER_NAME_KEY = "WINNER_NAME"

        fun newIntent(context: Context, winner: Team?): Intent {
            val intent = Intent(context, WinnerActivity::class.java)
            intent.putExtra(WINNER_NAME_KEY, winner)
            return intent
        }
    }

}