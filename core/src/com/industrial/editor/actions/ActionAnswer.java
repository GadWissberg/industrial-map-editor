package com.industrial.editor.actions;

public class ActionAnswer<T> {
	private final AnswerSubscriber<T> subscriber;

	public ActionAnswer(final AnswerSubscriber<T> answerSubscriber) {
		this.subscriber = answerSubscriber;
	}

	public void apply(final T data) {
		subscriber.onAnswerGiven(data);
	}
}
