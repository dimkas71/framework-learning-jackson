package ua.compservice.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@DisplayName("Annotations tests")
class AnnotationTests {

	@DisplayName("@JsonAlias annotation...")
	@Test
	void testAlias() throws JsonParseException, JsonMappingException, IOException {

		String json = "{\n" + "  \"n\": \"Dimkas\",\n" + "  \"age\": 46\n" + "}";

		ObjectMapper mapper = new ObjectMapper();

		Pojo aPojo = mapper.readValue(json, Pojo.class);

		assertThat(aPojo.getName(), is(equalTo("Dimkas")));
		assertThat(aPojo.getAge(), is(equalTo(46)));

		String anotherJson = "{\n" + "  \"someName\": \"Dimkas\",\n" + "  \"someAge\": 46\n" + "}";

		mapper = new ObjectMapper();

		aPojo = mapper.readValue(anotherJson, Pojo.class);

		assertThat(aPojo.getName(), is(equalTo("Dimkas")));
		assertThat(aPojo.getAge(), is(equalTo(46)));

	}

	@DisplayName("@JsonAnySetter with using method annotation")
	@Test
	void testJsonAnySetter() throws JsonParseException, JsonMappingException, IOException {

		String json = "{\n" + "  \"n\": \"Dimkas\",\n" + "  \"age\": 46,\n" + "  \"prop1\": 1,\n"
				+ "  \"prop2\": \"Some string\",\n" + "  \"prop3\": true\n" + "}";

		ObjectMapper mapper = new ObjectMapper();

		AnySetterPojo pojo = mapper.readValue(json, AnySetterPojo.class);

		assertThat(pojo.getName(), is(equalTo("Dimkas")));
		assertThat(pojo.getAge(), is(equalTo(46)));
		assertThat(pojo.map.size(), is(equalTo(3)));

		assertThat((Integer) pojo.map.get("prop1"), is(equalTo(1)));
		assertThat((String) pojo.map.get("prop2"), is(equalTo("Some string")));
		assertThat((Boolean) pojo.map.get("prop3"), is(true));

	}

	@DisplayName("@JsonAnySetter with using a field of Map type")
	@Test
	void testJsonAnySetterAsField() throws JsonParseException, JsonMappingException, IOException {

		String json = "{\n" + "  \"n\": \"Dimkas\",\n" + "  \"age\": 46,\n" + "  \"prop1\": 1,\n"
				+ "  \"prop2\": \"Some string\",\n" + "  \"prop3\": true\n" + "}";

		ObjectMapper mapper = new ObjectMapper();

		AnySetterWithMapPojo pojo = mapper.readValue(json, AnySetterWithMapPojo.class);

		assertThat(pojo.getName(), is(equalTo("Dimkas")));
		assertThat(pojo.getAge(), is(equalTo(46)));
		assertThat(pojo.withMap().size(), is(equalTo(3)));

		assertThat((Integer) pojo.withMap().get("prop1"), is(equalTo(1)));
		assertThat((String) pojo.withMap().get("prop2"), is(equalTo("Some string")));
		assertThat((Boolean) pojo.withMap().get("prop3"), is(true));

	}

}

class AnySetterWithMapPojo {

	@JsonAlias({ "n" })
	private String name;
	private int age;

	@JsonAnySetter
	private Map<String, Object> map = new HashMap<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Map<String, Object> withMap() {
		return Collections.unmodifiableMap(map);
	}

}

class AnySetterPojo {

	private String name;

	private int age;

	public String getName() {
		return name;
	}

	@JsonAlias({ "n" })
	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	Map<String, Object> map = new HashMap<String, Object>();

	@JsonAnySetter
	public void setter(String property, Object value) {
		map.put(property, value);
	}
}

class Pojo {

	@JsonAlias(value = { "n", "someName" })
	private String name;

	@JsonAlias(value = { "someAge" })
	private int age;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

}
