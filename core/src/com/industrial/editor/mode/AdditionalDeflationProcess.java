package com.industrial.editor.mode;

import com.google.gson.JsonObject;
import com.industrial.editor.model.elements.PlacedElement;

public interface AdditionalDeflationProcess {
	void run(JsonObject jsonObject, PlacedElement placedElement);
}
