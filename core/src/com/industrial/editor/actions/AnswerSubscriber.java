package com.industrial.editor.actions;

public interface AnswerSubscriber<T> {
	void onAnswerGiven(T data);
}
