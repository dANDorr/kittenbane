package com.example.kittenbane;

public class User {
	String name;
	Long shortestEasy;
	Long shortestMed;
	Long shortestHard;
	int bestScore;

	public User() {
		name = null;
		shortestEasy = null;
		shortestMed = null;
		shortestHard = null;
	}

	public User(String name, Long shortestEasy, Long shortestMed,
			Long shortestHard, int bestScore) {
		this.name = name;
		this.shortestEasy = shortestEasy;
		this.shortestMed = shortestMed;
		this.shortestHard = shortestHard;
		this.bestScore = bestScore;
	}

	public User(String name) {
		this.name = name;
		shortestEasy = null;
		shortestMed = null;
		shortestHard = null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getShortestEasy() {
		return shortestEasy;
	}

	public void setShortestEasy(Long shortestEasy) {
		this.shortestEasy = shortestEasy;
	}

	public Long getShortestMed() {
		return shortestMed;
	}

	public void setShortestMed(Long shortestMed) {
		this.shortestMed = shortestMed;
	}

	public Long getShortestHard() {
		return shortestHard;
	}

	public void setShortestHard(Long shortestHard) {
		this.shortestHard = shortestHard;
	}

	public int getBestScore() {
		return bestScore;
	}

	public void setBestScore(int bestScore) {
		this.bestScore = bestScore;
	}

	public String getSaveString() {
		return name + " " + shortestEasy + " " + shortestMed + " "
				+ shortestHard + " " + bestScore;
	}

}
