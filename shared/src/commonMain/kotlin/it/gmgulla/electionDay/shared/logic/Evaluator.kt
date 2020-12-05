package it.gmgulla.electionDay.shared.logic

import it.gmgulla.electionDay.shared.model.entities.Candidate
import kotlin.math.floor

class Evaluator(
    private val candidates: List<Candidate>,
    private val countedVotesPercentage: Double
) {
/*
    fun calculateVotesToWin(): Long {
        val defaultIndex = 0
        val countedVotes = calculateCountedVotesByCandidate(candidates[defaultIndex])
        val totalVotes = ((countedVotes * 100) / countedVotesPercentage).toLong()
        val uncountedVotes = totalVotes - countedVotes
        val ranking = candidates.sortedByDescending { it.votesReceived }
        return calculateVotesToWinByRanking(ranking, uncountedVotes)
    }

    private fun calculateVotesToWinByRanking(ranking: List<Candidate>, uncountedVotes: Long): Long {
        val forkBetweenTopTwoRankedCandidates = ranking[0].votesReceived - ranking[1].votesReceived
        return floor(
            (((uncountedVotes - forkBetweenTopTwoRankedCandidates) / 2) + 1).toDouble()
        ).toLong()
    }

    companion object {
        fun calculateCountedVotesByCandidate(aCandidate: Candidate): Long {
            return ((aCandidate.votesReceived * 100) / aCandidate.votesPercentage).toLong()
        }
    }

 */

}
