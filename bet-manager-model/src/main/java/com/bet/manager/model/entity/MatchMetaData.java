package com.bet.manager.model.entity;

import jdk.nashorn.internal.ir.annotations.Ignore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

@Entity
@Table(name = "match_metadata")
public class MatchMetaData implements Serializable {

	@Ignore
	@Transient
	public static final String SPLITERATOR = ",";

	private static final long serialVersionUID = -2793064452086787994L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Lob
	@CollectionTable(name = "first_team_metadata")
	private TreeMap<String, Object> firstTeamMetaData;

	@Lob
	@CollectionTable(name = "second_team_metadata")
	private TreeMap<String, Object> secondTeamMetaData;

	public MatchMetaData() {

	}

	public TreeMap<String, Object> getFirstTeamMetaData() {
		return firstTeamMetaData;
	}

	public void setFirstTeamMetaData(TreeMap<String, Object> firstTeamMetaData) {
		this.firstTeamMetaData = firstTeamMetaData;
	}

	public TreeMap<String, Object> getSecondTeamMetaData() {
		return secondTeamMetaData;
	}

	public void setSecondTeamMetaData(TreeMap<String, Object> secondTeamMetaData) {
		this.secondTeamMetaData = secondTeamMetaData;
	}

	@Override
	public String toString() {

		StringBuilder output = new StringBuilder();

		appendProperties(output, firstTeamMetaData);
		appendProperties(output, secondTeamMetaData);

		output.setLength(output.length() - 1);
		return output.toString();
	}

	private void appendProperties(StringBuilder output, Object property) {

		if (property instanceof Collection<?>) {
			for (Object o : ((Collection) property))
				appendProperties(output, o);
		} else if (property instanceof Map<?, ?>) {
			for (Object o : ((Map) property).entrySet())
				appendProperties(output, ((Map.Entry<?, ?>) o).getValue());
		} else
			output.append(property.toString()).append(SPLITERATOR);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof MatchMetaData))
			return false;

		MatchMetaData that = (MatchMetaData) o;

		return firstTeamMetaData.equals(that.firstTeamMetaData) && secondTeamMetaData.equals(that.secondTeamMetaData);
	}

	@Override
	public int hashCode() {
		int result = firstTeamMetaData.hashCode();
		result = 31 * result + secondTeamMetaData.hashCode();
		return result;
	}
}
