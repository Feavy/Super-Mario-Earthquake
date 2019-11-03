package fr.feavy.event.events;

import java.util.List;

import fr.feavy.utils.Question;

public class StartQuizzEvent extends Event{

	private List<Question> quizz;
	
	public StartQuizzEvent(List<Question> quizz) {
		super("startQuizzEvent");
		this.quizz = quizz;
	}
	
	public List<Question> getQuestions(){return quizz;}

}
