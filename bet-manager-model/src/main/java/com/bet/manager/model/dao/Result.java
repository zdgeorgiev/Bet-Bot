package com.bet.manager.model.dao;

public abstract class Result {

	/**
	 * score in format specified in your sub class
	 */
	protected String score;

	/**
	 * name of the home team
	 */
	protected String homeTeam;

	/**
	 * name of the away team
	 */
	protected String awayTeam;

	/**
	 * winner of the current match
	 */
	protected String winner;

	/**
	 * scoreDelimiter that is used in the score
	 * example: 2-2 (scoreDelimiter is - )
	 */
	protected String scoreDelimiter;

	public String getScore() {
		return score;
	}

	public String getHomeTeam() {
		return homeTeam;
	}

	public String getWinner() {
		return winner;
	}

	public String getAwayTeam() {
		return awayTeam;
	}

	public String getScoreDelimiter() {
		return scoreDelimiter;
	}

	public void setHomeTeam(String homeTeam) {
		this.homeTeam = homeTeam;
	}

	public void setAwayTeam(String awayTeam) {
		this.awayTeam = awayTeam;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	public void setScoreDelimiter(String scoreDelimiter) {
		this.scoreDelimiter = scoreDelimiter;
	}

	public void setScore(String score) {
		this.score = score;
	}

	@Override public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Result result = (Result) o;

		if (score != null ? !score.equals(result.score) : result.score != null)
			return false;
		if (homeTeam != null ? !homeTeam.equals(result.homeTeam) : result.homeTeam != null)
			return false;
		if (awayTeam != null ? !awayTeam.equals(result.awayTeam) : result.awayTeam != null)
			return false;
		if (winner != null ? !winner.equals(result.winner) : result.winner != null)
			return false;
		return scoreDelimiter != null ? scoreDelimiter.equals(result.scoreDelimiter) : result.scoreDelimiter == null;

	}

	@Override public int hashCode() {
		int result = score != null ? score.hashCode() : 0;
		result = 31 * result + (homeTeam != null ? homeTeam.hashCode() : 0);
		result = 31 * result + (awayTeam != null ? awayTeam.hashCode() : 0);
		result = 31 * result + (winner != null ? winner.hashCode() : 0);
		result = 31 * result + (scoreDelimiter != null ? scoreDelimiter.hashCode() : 0);
		return result;
	}
}
