package fr.feavy.utils;

public class Question {
	
	private String[] answers;
	private String question;
	private int goodAnswerIndex;
	
	public Question(String question, int goodAnswerIndex, String[] answers){
		this.answers = answers;
		this.goodAnswerIndex = goodAnswerIndex;
		this.question = question;
	}
	
	public String getQuestion(){return question;}
	public String[] getAnswers(){return answers;}
	public int getGoodIndex(){return goodAnswerIndex;}
	
}
