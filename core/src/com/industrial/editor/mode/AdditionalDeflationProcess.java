package com.industrial.editor.mode;

import com.gadarts.industrial.shared.model.ElementDefinition;
import com.google.gson.JsonObject;

public interface AdditionalDeflationProcess {
	void run(JsonObject jsonObject, ElementDefinition definition);
}
