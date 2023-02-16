package com.industrial.editor.mode;

import com.gadarts.industrial.shared.model.ElementDeclaration;
import com.google.gson.JsonObject;
import com.industrial.editor.model.elements.PlacedElement;

public interface AdditionalInflationProcess {
	void run(JsonObject jsonObject, ElementDeclaration definition, PlacedElement placedElement);
}
