package com.example.scorecounter

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.scorecounter.databinding.TeamListItemBinding
import com.example.scorecounter.databinding.WinnerListItemBinding

class TeamAdapter(val teams: ArrayList<Team>, private val viewType: Int = TEAM_ITEM_TYPE) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TEAM_ITEM_TYPE) {
            val binding =
                TeamListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            TeamViewHolder(binding)
        } else {
            val binding =
                WinnerListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            TeamWithScoreViewHolder(binding)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TeamViewHolder -> holder.bind(teams[position])
            is TeamWithScoreViewHolder -> holder.bind(teams[position])
        }
    }

    override fun getItemCount(): Int = teams.size

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

    companion object {
        const val TEAM_ITEM_TYPE = R.layout.team_list_item
        const val WINNER_ITEM_TYPE = R.layout.winner_list_item
    }

}

class TeamViewHolder(private val binding: TeamListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(team: Team) {
        binding.root.background.apply {
            setTint(team.colorResource)
            setTintMode(PorterDuff.Mode.MULTIPLY)
        }
        binding.tvTeamName.text = team.name
    }
}

class TeamWithScoreViewHolder(private val binding: WinnerListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(team: Team) {
        binding.root.background.apply {
            setTint(team.colorResource)
            setTintMode(PorterDuff.Mode.MULTIPLY)
        }
        binding.tvWinnerName.text = team.name
        binding.tvWinnerScore.text = team.score.toString()
    }
}