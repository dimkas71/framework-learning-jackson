package ua.compservice.test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

class MixinAnnotationTest {

	@DisplayName("When a target class serialized than we get correct result")
	@Test
	void whenAMixedClassSerialized_thanCorrect() throws JsonProcessingException {
		
		Target target = new Target("Dimkas", 46);
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new MyModule());
		String json = mapper.writeValueAsString(target);
		
		assertThat(json, containsString("myName"));
		assertThat(json, containsString("myAge"));
	}
	
	@DisplayName("When a target class deserialized than we get correct result")
	@Test
	void whenAMixedClassDeserialized_thenCorrect() throws JsonParseException, JsonMappingException, IOException {
		String sourceJson = "{\"someName\":\"Dimkas\",\"someAge\":46}";
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new MyModule());
		
		Target t = mapper.readValue(sourceJson, Target.class);
		
		assertThat(t.getName(), is(equalTo("Dimkas")));
		assertThat(t.getAge(), is(equalTo(46)));
		
	}
	
}

class MyModule extends SimpleModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MyModule() {
		super("MyModule", Version.unknownVersion());
	}

	@Override
	public void setupModule(SetupContext context) {
		context.setMixInAnnotations(Target.class, MixinTarget.class);
	}
	
	
	
	
	
}

abstract class MixinTarget {
	MixinTarget(@JsonProperty("someName") String name, @JsonProperty("someAge") int age) { }
	
	 // note: could alternatively annotate fields "w" and "h" as well -- if so, would need to @JsonIgnore getters
	  @JsonProperty("myName") abstract String getName(); // rename property
	  @JsonProperty("myAge") abstract int getAge(); // rename property
}

class Target {
	
	private final String name;
	private final int age;
	
	public Target(String name, int age) {
		super();
		this.name = name;
		this.age = age;
	}
	
	public String getName() {
		return name;
	}
	public int getAge() {
		return age;
	}
	
}
