package fr.feavy.event.events;

public class QuizzQuestionAnsweredEvent extends Event{

	private boolean isGoodAnswer;
	private int questionIndex, magnitude, deathAmount;
	
	public QuizzQuestionAnsweredEvent(int questionIndex) {
		super("quizzQuestionAnsweredEvent");
		this.isGoodAnswer = true;
		this.questionIndex = questionIndex;
		this.magnitude = 0;
		this.deathAmount = 0;
	}
	
	public QuizzQuestionAnsweredEvent(int questionIndex, int magnitude, int deathAmount){
		super("quizzQuestionAnsweredEvent");
		this.isGoodAnswer = false;
		this.questionIndex = questionIndex;
		this.magnitude = magnitude;
		this.deathAmount = deathAmount;
	}
	
	public boolean isGoodAnswer(){return isGoodAnswer;}
	public int getQuestionIndex(){return questionIndex;}
	public int getDeathAmount(){return deathAmount;}
	public int getMagnitude(){return magnitude;}
	
}
