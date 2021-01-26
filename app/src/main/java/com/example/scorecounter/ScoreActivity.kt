package com.example.scorecounter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.example.scorecounter.databinding.ScoreScreenLayoutBinding

class ScoreActivity : AppCompatActivity() {
    lateinit var binding: ScoreScreenLayoutBinding
    private var countdown: Long = 20
    private val teams: ArrayList<Team> = arrayListOf()
    private var gameStarted = false
    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ScoreScreenLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = getString(R.string.score_screen_title)

        setTeams()
        setClickListeners()
    }

    override fun onStop() {
        super.onStop()

        timer?.cancel()
    }

    override fun onRestart() {
        super.onRestart()

        restartTimer()
    }

    override fun onBackPressed() {
        cancelGame()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.score_screen_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.cancel_game -> {
                cancelGame()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun cancelGame() {
        timer?.cancel()
        MaterialDialog(this).show {
            title(R.string.cancel_game)
            positiveButton(R.string.yes) {
                val intent = MainActivity.newIntent(this@ScoreActivity)
                startActivity(intent)
            }
            negativeButton(R.string.no) {
                it.dismiss()
            }
            setOnDismissListener {
                restartTimer()
            }
        }
    }

    private fun restartTimer() {
        if (gameStarted) {
            timer = createTimer().apply {
                start()
            }
        }
    }


    private fun setClickListeners() {

        binding.tvTeam1Score.setOnClickListener {
            if (gameStarted) {
                val team = teams[TeamIndex.TEAM_ONE.index]
                team.score++
                binding.tvTeam1Score.text = team.score.toString()
            } else {
                Toast.makeText(this, getString(R.string.game_not_started), Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.tvTeam2Score.setOnClickListener {
            if (gameStarted) {
                val team = teams[TeamIndex.TEAM_TWO.index]
                team.score++
                binding.tvTeam2Score.text = team.score.toString()
            } else {
                Toast.makeText(this, getString(R.string.game_not_started), Toast.LENGTH_SHORT)
                    .show()
            }

        }

        binding.tvTimer.setOnClickListener {
            gameStarted = true
            timer = createTimer().apply {
                start()
            }
            it.isEnabled = false
            it.setOnClickListener(null)
        }
    }

    private fun setTeams() {
        val teams = intent.getParcelableArrayListExtra<Team>(TEAM_NAMES_KEY)
        teams?.apply {
            this@ScoreActivity.teams.addAll(this)
            val teamOne = this[TeamIndex.TEAM_ONE.index]
            val teamTwo = this[TeamIndex.TEAM_TWO.index]

            binding.tvTeam1Name.text = teamOne.name
            binding.tvTeam1Name.setTextColor(teamOne.colorResource)
            binding.tvTeam2Name.text = teamTwo.name
            binding.tvTeam2Name.setTextColor(teamTwo.colorResource)

            binding.tvTeam1Score.setTextColor(teamOne.colorResource)
            binding.tvTeam2Score.setTextColor(teamTwo.colorResource)
        } ?: run {
            finish()
        }
    }

    private fun selectWinner() {
        val teamOne = teams[TeamIndex.TEAM_ONE.index]
        val teamTwo = teams[TeamIndex.TEAM_TWO.index]

        when {
            teamOne.score > teamTwo.score -> {
                val intent = WinnerActivity.newIntent(this@ScoreActivity, teams[0])
                startActivity(intent)
            }
            teamOne.score < teamTwo.score -> {
                val intent = WinnerActivity.newIntent(this@ScoreActivity, teams[1])
                startActivity(intent)
            }
            else -> {
                val intent = WinnerActivity.newIntent(this@ScoreActivity, null)
                startActivity(intent)
            }
        }
    }

    private fun createTimer(): CountDownTimer {
        return object : CountDownTimer(countdown * 1000, 1000) {
            override fun onTick(timeLeft: Long) {
                if (timeLeft <= 6000) {
                    val timerColor = getColor(R.color.solid_red)
                    binding.tvTimer.setTextColor(timerColor)
                }
                binding.tvTimer.text = (--countdown).toString()
            }

            override fun onFinish() {
                selectWinner()
            }

        }
    }

    companion object {
        const val TEAM_NAMES_KEY = "TEAM_NAMES"

        fun newIntent(context: Context, teams: ArrayList<Team>): Intent {
            val intent = Intent(context, ScoreActivity::class.java)
            intent.putParcelableArrayListExtra(TEAM_NAMES_KEY, teams)
            return intent
        }
    }

}